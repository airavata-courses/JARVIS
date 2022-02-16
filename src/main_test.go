package main

import (
    "testing"
)

func TestAddingToCache(t *testing.T) {
    var req WeatherReq
    _,res := LocalDbLookup(req)
    if res != false {
        t.Errorf("Empty db lookup resulted a result")
    }

    imdb = append(imdb, ImDb{ImgUrl:"abcd", Datetime: "dt", Location: "home"})

    req.Datetime = "dt"
    req.Location = "home"
    lookupv,res2 := LocalDbLookup(req)
    if res2 != true {
        t.Errorf("db lookup did not result in a result")
        t.Error(lookupv)
    }

    req.Datetime = "dt"
    req.Location = "myhome"
    lookupv2,res3 := LocalDbLookup(req)
    if res3 != false {
        t.Errorf("Wrong entry db lookup resulted in a result")
        t.Error(lookupv2)
    }
}
