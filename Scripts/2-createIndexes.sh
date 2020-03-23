DIR="./"
DIR_COLL=$DIR"Collection/"

DIR_INDX=$DIR"Indexes/"
DIR_INDX_DS=$DIR_INDX"DefautStemmer/"
DIR_INDX_ES=$DIR_INDX"EnglishStemmer/"
DIR_INDX_ESS=$DIR_INDX"EnglishStemmerStopwords/"

mkdir $DIR_INDX
mkdir $DIR_INDX_DS
mkdir $DIR_INDX_ES
mkdir $DIR_INDX_ESS

cp $DIR_COLL"cran.collection" $DIR_INDX_DS"cran.collection"
cp $DIR_COLL"cran.collection" $DIR_INDX_ES"cran.collection"
cp $DIR_COLL"cran.collection" $DIR_INDX_ESS"cran.collection"

java it.unimi.di.big.mg4j.tool.IndexBuilder -S $DIR_COLL"cran.collection" $DIR_INDX_DS"cran"

java it.unimi.di.big.mg4j.tool.IndexBuilder -t it.unimi.di.big.mg4j.index.snowball.EnglishStemmer -S $DIR_COLL"cran.collection" $DIR_INDX_ES"cran"

java it.unimi.di.big.mg4j.tool.IndexBuilder -t homework.EnglishStemmerStopwords -S $DIR_COLL"cran.collection" $DIR_INDX_ESS"cran"
