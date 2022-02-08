package main

import(
    "bytes"
    "encoding/json"
    "fmt"
    "log"
    "net/http"
    "github.com/gorilla/mux"
)

var authserv string = "https://12877c5d-aa48-4583-b999-321b06efc7ca.mock.pstmn.io"+"/api/check_token"
var s3serv string = "http://s3get_server"+"/api/nexraddata"
var dbserv string = "http://dbapp"+"/addUserSearchRecord"


type AuthservReq struct{
    SessionId string `json:"session_id"`
}

type AuthservResp struct{
    UserId string `json:"user_id"`
}

func authUser(sess_id string, resp *AuthservResp) {
    var req AuthservReq
    req.SessionId = sess_id

    postbody, _ := json.Marshal(req)
    reqbody := bytes.NewBuffer(postbody)

    respbody, err := http.Post(authserv, "application/json", reqbody)
    if  err != nil{
        log.Fatalln(err)
    }

    err = json.NewDecoder(respbody.Body).Decode(resp)
    if err != nil {
        log.Fatalln(err)
    }
}

type S3Resp struct{
    ImgUrl string `json:"img_url"`
}
func reqS3(req WeatherReq, resp *S3Resp){

    postbody, _ := json.Marshal(req)
    reqbody := bytes.NewBuffer(postbody)

    respbody, err := http.Post(s3serv, "application/json", reqbody)
    if  err != nil{
        log.Fatalln(err)
    }

    err = json.NewDecoder(respbody.Body).Decode(resp)
    if err != nil {
        log.Fatalln(err)
    }
}

type UserActionReq struct{
    Datetime string `json:"searched_time"`
    Location string `json:"place_name"`
    UserId string `json:"user_unique_id"`
    ImgUrl string `json:"data_link"`
    LocationSearchedAt string `json:"location_searched_at"`
}

func recordUserAction(wreq WeatherReq, uid string, imgurl string) {
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
        log.Fatalln(err)
    }
    fmt.Println("Got data back")
    fmt.Println(respbody)
}

type WeatherReq struct {
    Datetime string `json:"date_time"`
    Location string `json:"station"`
    Session_id string `json:"session_id"`
}

type WeatherResp struct {
    ImgUrl string `json:"img_url"`
}

func getWeather(w http.ResponseWriter, r *http.Request){

    var wetreq WeatherReq
    _ = json.NewDecoder(r.Body).Decode(&wetreq)

    fmt.Println("Received request", wetreq)

    // Authenticate the user
    var auth AuthservResp
    authUser(wetreq.Session_id, &auth)
    fmt.Println("got data back from auth serv", auth)

    // Check local cache
    var resp WeatherResp
    img, found := LocalDbLookup(wetreq)
    if found {
        fmt.Println("Found data locally", img)
        resp.ImgUrl = img.ImgUrl
    } else {
        // request for map from s3
        var s3ret S3Resp
        reqS3(wetreq, &s3ret)
        fmt.Println("got data back from s3 serv", s3ret)
        resp.ImgUrl = s3ret.ImgUrl
        // Add to local cache
        imdb = append(imdb, ImDb{ImgUrl:resp.ImgUrl, Datetime: wetreq.Datetime, Location: wetreq.Location})
    }
    // save to file

    // record user action
    recordUserAction(wetreq, auth.UserId, resp.ImgUrl)

    // send response
    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(resp)
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


