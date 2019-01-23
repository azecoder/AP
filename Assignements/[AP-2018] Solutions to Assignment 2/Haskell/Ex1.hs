module Ex1 (
    ListBag(LB),
    wf,
    empty,
    singleton,
    fromList,
    toList,
    isEmpty,
    mul,
    sumBag
) where

import Data.List  

data ListBag a = LB [(a, Int)] deriving (Show, Eq)

-- well-formed
wf :: Eq a => ListBag a -> Bool
wf (LB []) = True
wf (LB ( (x, m):xs) ) = 
    ( notElem x ( [ fst ix | ix <- xs ] ) ) 
    && ( wf ( LB xs ) )

-- empty, that returns an empty ListBag
empty :: ListBag a
empty = LB []

-- singleton v, returning a ListBag containing just one occurrence of element v
singleton :: a -> ListBag a
singleton v = LB [(v, 1)]

-- fromList lst, returning a ListBag containing all and only the elements of lst, each with the right multiplicity
fromList :: Ord a => [a] -> ListBag a
fromList = LB . map (\x -> (head x, length x)) . group . sort

-- isEmpty bag, returning True if and only if bag is empty
isEmpty :: ListBag a -> Bool
isEmpty (LB []) = True
isEmpty (LB _) = False

-- mul v bag, returning the multiplicity of v in the ListBag bag if v is an element of bag, and 0 otherwise
mul :: Eq a => a -> ListBag a -> Int
mul _ (LB []) = 0
mul v (LB ((x,c):bag)) = if x == v then c else mul v (LB bag)

-- toList bag, that returns a list containing all the elements of the ListBag bag, each one repeated a number of times equal to its multiplicity
toList :: ListBag a -> [a]
toList (LB []) = []
toList (LB ((x, m):xs)) = [x | ix <- [1 .. m]] ++ (toList ( LB xs ) )


-- sumBag bag bag', returning the ListBag obtained by adding all the elements of bag' to bag
sumBag :: Ord a => ListBag a -> ListBag a -> ListBag a
sumBag bag bag' = fromList ( (toList bag) ++ (toList bag') )

-- let ll = LB [("g",3),("b",6)]
-- isEmpty ll
-- ll = empty
-- let ll = LB [("g",3),("b",6),("f",8),("d",2),("a",3),("c",2),("z",0)]
-- mul "f" ll
-- let tl = toList ll
-- let fl = fromList ll
-- let nl = [("b",6),("f",8),("d",2)]
-- let sl = sumBag ll nl
-- wf sl