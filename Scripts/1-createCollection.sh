DIR="./"
DIR_COLL=$DIR"Collection/"

mkdir $DIR_COLL

find cran -iname \*.html | java it.unimi.di.big.mg4j.document.FileSetDocumentCollection -f HtmlDocumentFactory -p encoding=UTF-8 $DIR_COLL"cran.collection"
