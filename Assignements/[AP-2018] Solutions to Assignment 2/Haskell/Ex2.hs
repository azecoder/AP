module Ex2 ( 
    mapLB
) where

import Ex1

-- Define an instance of the constructor class Foldable for the constructor ListBag defined in Exercise 1. 
-- To this aim, choose a minimal set of functions to be implemented, as described in the documentation of Foldable. 
-- Intuitively, folding a ListBag with a binary function should apply 
-- the function to the elements of the multiset, ignoring the multiplicities.
instance Foldable ListBag where
    foldr f x (LB l) = foldr f x ( map fst l )
-- made list contains only elements
-- and then fold over this list

-- Define a function mapLB that takes a function f :: a -> b and a ListBag of type a as an argument, 
-- and returns the ListBag of type b obtained by applying f to all the elements of its second argument.
mapLB :: Ord b => (a -> b) -> ListBag a -> ListBag b
mapLB f bag = fromList ( map f ( toList bag ) )
-- convert the input ListBag to a list
-- then map the function on the elements of the list
-- and construct the ListBag from the mapped list




-- In Ex1, we wrote the function fromList to get list of the elements and their right multiplicities. 
-- And we ordered the elemets of the list. 
-- We also use same functionality in mapLB. Because of that, the result is well formed too.
-- This constraint shows us, it is not possible to give an instance of Functor
-- for ListBag with mapLB as fmap
