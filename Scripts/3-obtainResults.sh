DIR="./"
DIR_RES=$DIR"QueriesResults/"
DIR_COLL=$DIR"Collection/"

DIR_INDX=$DIR"Indexes/"
DIR_INDX_DS=$DIR_INDX"DefautStemmer/"
DIR_INDX_ES=$DIR_INDX"EnglishStemmer/"
DIR_INDX_ESS=$DIR_INDX"EnglishStemmerStopwords/"

mkdir $DIR_RES

################## Default Stemmer Stopwords ##########################
DIR_DS=$DIR_RES"DefaultStemmer/"
DIR_DS_Te=$DIR_DS"DSText/"
DIR_DS_Ti=$DIR_DS"DSTitle/"
DIR_DS_TT=$DIR_DS"DSTextAndTitle/"

mkdir $DIR_DS
mkdir $DIR_DS_Te
mkdir $DIR_DS_Ti
mkdir $DIR_DS_TT

# text
java homework.RunAllQueries_HW $DIR_INDX_DS"cran" $DIR"Queries/cran_all_queries.tsv" "CountScorer" "text" $DIR_DS_Te"output_text_DefaultStemmer_CountScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_DS"cran" $DIR"Queries/cran_all_queries.tsv" "TfIdfScorer" "text" $DIR_DS_Te"output_text_DefaultStemmer_TfIdfScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_DS"cran" $DIR"Queries/cran_all_queries.tsv" "BM25Scorer" "text" $DIR_DS_Te"output_text_DefaultStemmer_BM25Scorer.tsv"

# title
java homework.RunAllQueries_HW $DIR_INDX_DS"cran" $DIR"Queries/cran_all_queries.tsv" "CountScorer" "title" $DIR_DS_Ti"output_title_DefaultStemmer_CountScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_DS"cran" $DIR"Queries/cran_all_queries.tsv" "TfIdfScorer" "title" $DIR_DS_Ti"output_title_DefaultStemmer_TfIdfScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_DS"cran" $DIR"Queries/cran_all_queries.tsv" "BM25Scorer" "title" $DIR_DS_Ti"output_title_DefaultStemmer_BM25Scorer.tsv"

# title and text
java homework.RunAllQueries_HW $DIR_INDX_DS"cran" $DIR"Queries/cran_all_queries.tsv" "CountScorer" "text_and_title" $DIR_DS_TT"output_TextAndTitle_DefaultStemmer_CountScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_DS"cran" $DIR"Queries/cran_all_queries.tsv" "TfIdfScorer" "text_and_title" $DIR_DS_TT"output_TextAndTitle_DefaultStemmer_TfIdfScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_DS"cran" $DIR"Queries/cran_all_queries.tsv" "BM25Scorer" "text_and_title" $DIR_DS_TT"output_TextAndTitle_DefaultStemmer_BM25Scorer.tsv"

################# English Stemmer #############################
DIR_ES=$DIR_RES"EnglishStemmer/"
DIR_ES_Te=$DIR_ES"ESText/"
DIR_ES_Ti=$DIR_ES"ESTitle/"
DIR_ES_TT=$DIR_ES"ESTextAndTitle/"

mkdir $DIR_ES
mkdir $DIR_ES_Te
mkdir $DIR_ES_Ti
mkdir $DIR_ES_TT

# text
java homework.RunAllQueries_HW $DIR_INDX_ES"cran" $DIR"Queries/cran_all_queries.tsv" "CountScorer" "text" $DIR_ES_Te"output_text_EnglishStemmer_CountScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ES"cran" $DIR"Queries/cran_all_queries.tsv" "TfIdfScorer" "text" $DIR_ES_Te"output_text_EnglishStemmer_TfIdfScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ES"cran" $DIR"Queries/cran_all_queries.tsv" "BM25Scorer" "text" $DIR_ES_Te"output_text_EnglishStemmer_BM25Scorer.tsv"

# title
java homework.RunAllQueries_HW $DIR_INDX_ES"cran" $DIR"Queries/cran_all_queries.tsv" "CountScorer" "title" $DIR_ES_Ti"output_title_EnglishStemmer_CountScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ES"cran" $DIR"Queries/cran_all_queries.tsv" "TfIdfScorer" "title" $DIR_ES_Ti"output_title_EnglishStemmer_TfIdfScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ES"cran" $DIR"Queries/cran_all_queries.tsv" "BM25Scorer" "title" $DIR_ES_Ti"output_title_EnglishStemmer_BM25Scorer.tsv"

# title and text
java homework.RunAllQueries_HW $DIR_INDX_ES"cran" $DIR"Queries/cran_all_queries.tsv" "CountScorer" "text_and_title" $DIR_ES_TT"output_TextAndTitle_EnglishStemmer_CountScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ES"cran" $DIR"Queries/cran_all_queries.tsv" "TfIdfScorer" "text_and_title" $DIR_ES_TT"output_TextAndTitle_EnglishStemmer_TfIdfScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ES"cran" $DIR"Queries/cran_all_queries.tsv" "BM25Scorer" "text_and_title" $DIR_ES_TT"output_TextAndTitle_EnglishStemmer_BM25Scorer.tsv"

################### English Stemmer Stopwords ############################
DIR_ESS=$DIR_RES"EnglishStemmerStopwords/"
DIR_ESS_Te=$DIR_ESS"ESSText/"
DIR_ESS_Ti=$DIR_ESS"ESSTitle/"
DIR_ESS_TT=$DIR_ESS"ESSTextAndTitle/"

mkdir $DIR_ESS
mkdir $DIR_ESS_Te
mkdir $DIR_ESS_Ti
mkdir $DIR_ESS_TT

# text
java homework.RunAllQueries_HW $DIR_INDX_ESS"cran" $DIR"Queries/cran_all_queries.tsv" "CountScorer" "text" $DIR_ESS_Te"output_text_EnglishStemmerStopwords_CountScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ESS"cran" $DIR"Queries/cran_all_queries.tsv" "TfIdfScorer" "text" $DIR_ESS_Te"output_text_EnglishStemmerStopwords_TfIdfScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ESS"cran" $DIR"Queries/cran_all_queries.tsv" "BM25Scorer" "text" $DIR_ESS_Te"output_text_EnglishStemmerStopwords_BM25Scorer.tsv"

# title
java homework.RunAllQueries_HW $DIR_INDX_ESS"cran" $DIR"Queries/cran_all_queries.tsv" "CountScorer" "title" $DIR_ESS_Ti"output_title_EnglishStemmerStopwords_CountScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ESS"cran" $DIR"Queries/cran_all_queries.tsv" "TfIdfScorer" "title" $DIR_ESS_Ti"output_title_EnglishStemmerStopwords_TfIdfScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ESS"cran" $DIR"Queries/cran_all_queries.tsv" "BM25Scorer" "title" $DIR_ESS_Ti"output_title_EnglishStemmerStopwords_BM25Scorer.tsv"

# title and text
java homework.RunAllQueries_HW $DIR_INDX_ESS"cran" $DIR"Queries/cran_all_queries.tsv" "CountScorer" "text_and_title" $DIR_ESS_TT"output_TextAndTitle_EnglishStemmerStopwords_CountScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ESS"cran" $DIR"Queries/cran_all_queries.tsv" "TfIdfScorer" "text_and_title" $DIR_ESS_TT"output_TextAndTitle_EnglishStemmerStopwords_TfIdfScorer.tsv"
java homework.RunAllQueries_HW $DIR_INDX_ESS"cran" $DIR"Queries/cran_all_queries.tsv" "BM25Scorer" "text_and_title" $DIR_ESS_TT"output_TextAndTitle_EnglishStemmerStopwords_BM25Scorer.tsv"
