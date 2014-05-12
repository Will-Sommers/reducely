(ns reducely.core
  (:require [ring.adapter.jetty :as jetty]))

(defn handler [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World from Reducely"})

(defn boot []
  (jetty/run-jetty #'handler {:port 8080 :join? false}))
