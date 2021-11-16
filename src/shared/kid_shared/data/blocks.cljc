(ns kid-shared.data.blocks (:require [kid-shared.types.post :as post]
                                      [kid-shared.types.user :as user]
                                      [kid-shared.resources.images :as images]))

;;
;; A datatype meant to be able to post certain news
;;  periodically
;;
;;

(def web-search-explanation
 [:<>
  [:p "Sometimes a simple web search is all you need to verify a post."]
  [:p "Just bring up your favorite search engine and type in a couple of relevant keywords. These can be taken straight from the text (like the name of an institution) or describe the content on a meta level (report on XYZ)."]
  [:p "To get good results, it's important to put multiword terms in quotes (e.g. 'European Union') and also use Boolean operators (e.g. \"European Union\" AND \"climate report\")."]
  [:p "A search for the right keywords often leads you to either an original source or a secondary piece of content that refers to it. You should then compare the content of these sources with what people claim on social media and boom! – you get a good hunch of what's legit and what's not."]
])

(def image-analysis-explanation
  [:<>
   [:p "An image analysis tells you whether or not an image has been tampered with. For a basic analysis, all the tools you need are your eyes."]
   [:p "Take a closer look at the image and ask yourself: Are there any weird looking spots? Is there something off about the perspective? About the lighting? The colors? Does it looks like objects or people have been added afterwards?"]
   [:p "If the answer to one or more of the above questions is \"yes\", you're probably looking at a manipulated image."]
   [:p "If you're not sure, use a digital zoom (or a set of forensics tools) to investigate further."]
   ]
)

(def ris-explanation
  [:<>
   [:p "Among other things, an RIS tells you whether or not an image has been used on the (open) web before -- and in which context."]

   [:p "To do an RIS hands down, install the so-called \"WeVerify Plugin\" for your browser. Once it’s activated, right-click on the web image you need to verify, choose a search engine (e.g. the one by Megacorp) – and wait for the RIS results to show up."]
 
  [:p "If the image you're verifying shows up in lots of other places on the web (especially with different dates and descriptions), it’s very likely you're NOT looking at an original.   Which means: People making weird claims about said image on Beeper are probably NOT telling the truth."]
   ]
  )

(def ris-crop-explanation
  [:<>
   [:p "Sometimes, an RIS doesn't yield any results. In that case, try cropping the picture, thus putting a focus on a certain part of it, e.g. a person or a building. Repeat the search. Refine the keywords if necessary."]

    [:p "Do the results show the person or building allegedly depicted in the uncropped version? If they don't, any claim about this image should be treated with utmost caution."]
 ])
