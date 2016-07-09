(ns mycotrack-frame.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [mycotrack-frame.core-test]))

(doo-tests 'mycotrack-frame.core-test)
