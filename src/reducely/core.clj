(ns reducely.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as mp]
            [hiccup.core :as h]
            [formative.core :as f]
            [formative.parse :as fp]
            [clojure.pprint :as pp]
            [reducely.db :as db]))


(defn gen-response [data]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body data})

(defn gen-redirect [url]
  {:status 302
   :headers {"location " url}})

(defn create [req]
  (let [url (get-in req [:params "url"])
        short (db/add-url url)]
    (gen-response (str "URL for" url "is now http://localhost:8080/links/" short))))

(defn redirect [id]
  (let [url (db/find-url id)]
    (gen-redirect url)))

(def form
  {:fields [{:name :url}]
   :validations [[:required [:url]]]
   :action "/create"})

(defn handler [req]
  (condp = (re-find #"\/[a-zA-Z0-9]+" (:uri req))
    "/create" (create req)
    "/links" (redirect (last
                        (re-find
                         #"\/links\/([a-zA-Z0-9]+)"
                         (:uri req))))
    (gen-response (h/html (f/render-form form)))))

(def app
  (mp/wrap-params handler))

(defn boot []
  (jetty/run-jetty #'app {:port 8080 :join? false}))

;; = /link/ => read from DB and redirect
;; = /create => write to DB with params
;; everything else render form?
