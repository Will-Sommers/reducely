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
  (if (= (:uri req) "/create")
    (gen-response (str "Hi, " (get-in req [:params "url"])))
    (gen-response (h/html (f/render-form form)))
    ))

(def app
  (mp/wrap-params handler))

(defn boot []
  (jetty/run-jetty #'app {:port 8080 :join? false}))
