(ns reducely.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as mp]
            [hiccup.core :as h]
            [formative.core :as f]
            [formative.parse :as fp]
            [clojure.pprint :as pp]))

(defn gen-response [data]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body data})

(def form
  {:fields [{:name :url}]
   :validations [[:required [:url]]]
   :action "/create"})

(defn handler [req]
  (condp = (re-find #"\/[a-zA-Z0-9]+" (:uri req))
    "/create" (gen-response (str "Hi, " (get-in req [:params "url"])))
    "/links" (gen-response "The links!")
    (gen-response (h/html (f/render-form form)))))

(def app
  (mp/wrap-params handler))

(defn boot []
  (jetty/run-jetty #'app {:port 8080 :join? false}))

;; = /link/ => read from DB and redirect
;; = /create => write to DB with params
;; everything else render form? 
