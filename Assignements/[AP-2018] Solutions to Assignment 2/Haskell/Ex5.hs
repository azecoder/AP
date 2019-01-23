module Ex5 ( 
    returnLB,
    bindLB
) where

import Ex1
import Ex2
import Control.Monad

-- Define the operations returnLB and bindLB, taking inspiration from the operations of the Monad type constructor.
returnLB :: a -> ListBag a
returnLB = singleton

bindLB :: Ord b => ListBag a -> (a -> ListBag b) -> ListBag b
bindLB (LB l) f =  foldr sumBag empty ( join [replicate m (f x) | (x, m) <- l] )
