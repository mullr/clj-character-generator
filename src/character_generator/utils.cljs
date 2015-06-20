(ns character-generator.utils)

(defn mapvals [m f]
  (->> m
       (map (fn [[k v]] [k (f v)]))
       (into {})))
