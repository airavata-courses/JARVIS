package main

import(
    "bytes"
    "encoding/json"
    "fmt"
    "log"
    "net/http"
    "github.com/gorilla/mux"
)

var authserv string = "http://authserver"+"/login_auth/verify_token"
var s3serv string = "http://s3getserver"+"/api/nexraddata"
var dbserv string = "http://dbapp"+"/addUserSearchRecord"


type AuthservReq struct{
    SessionId string `json:"session_id"`
}

type AuthservResp struct{
    Status string `json:"status"`
    DD *DecodedData `json:"DECODED_DATA"`
}

type DecodedData struct{
    Email string `json:"EMAIL_ID"`
    UID string `json:"UNIQUE_USER_ID"`
    LogDt string `json:"LOGIN_DATETIME"`
    iat int64 `json:"iat"`
    exp int64 `json:"exp"`
}

func authUser(sess_id string, resp *AuthservResp) int{
    var req AuthservReq
    req.SessionId = sess_id

    postbody, _ := json.Marshal(req)
    reqbody := bytes.NewBuffer(postbody)

    respbody, err := http.Post(authserv, "application/json", reqbody)
    if  err != nil{
        log.Println(err)
        return -1;
    }

    err = json.NewDecoder(respbody.Body).Decode(resp)
    if err != nil {
        log.Println(err)
        return -1;
    }
    if resp.Status == "error" {
        return -1;
    }
    return 0;
}

type S3Resp struct{
    Status string `json:"status"`
    ImgUrl string `json:"img_url"`
}
func reqS3(req WeatherReq, resp *S3Resp) int{

    postbody, _ := json.Marshal(req)
    reqbody := bytes.NewBuffer(postbody)

    respbody, err := http.Post(s3serv, "application/json", reqbody)
    if  err != nil{
        log.Println(err)
        return -1;
    }

    err = json.NewDecoder(respbody.Body).Decode(resp)
    if err != nil {
        log.Println(err)
        return -1;
    }
    if resp.Status == "error"{
        return -1;
    }
    return 0;
}

type UserActionReq struct{
    Datetime string `json:"searched_time"`
    Location string `json:"place_name"`
    UserId string `json:"user_unique_id"`
    ImgUrl string `json:"data_link"`
    LocationSearchedAt string `json:"location_searched_at"`
}

func recordUserAction(wreq WeatherReq, uid string, imgurl string) int{
    var req UserActionReq
    req.Datetime = wreq.Datetime
    req.Location = wreq.Location
    req.UserId = uid
    req.ImgUrl = imgurl
    req.LocationSearchedAt = wreq.Datetime

    fmt.Println("Adding user record")
    fmt.Println(req)
    postbody, _ := json.Marshal(req)
    reqbody := bytes.NewBuffer(postbody)

    respbody, err := http.Post(dbserv, "application/json", reqbody)
    if  err != nil{
        log.Println(err)
        return -1;
    }
    fmt.Println("Got data back")
    fmt.Println(respbody)
    return 0;
}

type WeatherReq struct {
    Datetime string `json:"date_time"`
    Location string `json:"station"`
    Session_id string `json:"session_id"`
}

type WeatherResp struct {
    Status string `json:"status"`
    ImgUrl string `json:"img_url"`
}

type WeatherErrResp struct {
    Status string `json:"status"`
    ErrMsg string `json:"message"`
}

func getWeather(w http.ResponseWriter, r *http.Request){

    var errresp WeatherErrResp
    var wetreq WeatherReq
    var resp WeatherResp
    var img ImDb
    var found bool

    _ = json.NewDecoder(r.Body).Decode(&wetreq)

    fmt.Println("Received request", wetreq)
    errresp.Status = "error"

    // Authenticate the user
    var auth AuthservResp
    if (authUser(wetreq.Session_id, &auth) != 0){
        errresp.ErrMsg = "Failed to authenticate"
        goto fail;
    }
    fmt.Println("got data back from auth serv", auth)
    fmt.Println("auth decoded", auth.DD)

    // Check local cache
    img, found = LocalDbLookup(wetreq)
    if found {
        fmt.Println("Found data locally", img)
        resp.ImgUrl = img.ImgUrl
    } else {
        // request for map from s3
        var s3ret S3Resp
        if (reqS3(wetreq, &s3ret) != 0){
            errresp.ErrMsg = "Failed to get data from s3"
            goto fail;
        }
        fmt.Println("got data back from s3 serv", s3ret)
        resp.ImgUrl = s3ret.ImgUrl
        // Add to local cache
        imdb = append(imdb, ImDb{ImgUrl:resp.ImgUrl, Datetime: wetreq.Datetime, Location: wetreq.Location})
    }
    // save to file

    // record user action
    if (recordUserAction(wetreq, auth.DD.UID, resp.ImgUrl) != 0){
        errresp.ErrMsg = "Failed to record user actions"
        goto fail;
    }

    // send response
    resp.Status = "success"
    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(resp)
    return

    fail:
    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(errresp)
    return
}

type ImDb struct {
    Datetime string
    Location string
    ImgUrl string
}

var imdb []ImDb

func LocalDbLookup(req WeatherReq) (ImDb,bool){
    for _, item := range imdb {
        if item.Datetime == req.Datetime && item.Location == req.Location  {
            fmt.Println(item)
            return item, true
        }
    }
    return ImDb{},false
}

func main(){
    fmt.Println("Starting cache server")

    // Load initial array by doing a scan from fs

    // Inti Router 
    r := mux.NewRouter()

    // Router Handler
    r.HandleFunc("/api/getweather", getWeather).Methods("POST")

    // Start web server
    log.Fatal(http.ListenAndServe(":80", r))

}
