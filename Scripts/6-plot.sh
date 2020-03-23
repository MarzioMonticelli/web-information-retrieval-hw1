# Constants
DIROUT="./Plots/"
mkdir $DIROUT

DIR_M="./"
DIR_QRES=$DIR_M"QueriesResults/"
DIR_RES=$DIR_M"Queries/"

DIR_DS_TT=$DIR_QRES"DefaultStemmer/DSTextAndTitle/"
DIR_ES_TT=$DIR_QRES"EnglishStemmer/ESTextAndTitle/"
DIR_ESS_TT=$DIR_QRES"EnglishStemmerStopwords/ESSTextAndTitle/"

DSBM25=$DIR_DS_TT"output_TextAndTitle_DefaultStemmer_BM25Scorer.tsv"
DSCOUN=$DIR_DS_TT"output_TextAndTitle_DefaultStemmer_CountScorer.tsv"
DSTFIF=$DIR_DS_TT"output_TextAndTitle_DefaultStemmer_TfIdfScorer.tsv"

ESBM25=$DIR_ES_TT"output_TextAndTitle_EnglishStemmer_BM25Scorer.tsv"
ESCOUN=$DIR_ES_TT"output_TextAndTitle_EnglishStemmer_CountScorer.tsv"
ESTFIF=$DIR_ES_TT"output_TextAndTitle_EnglishStemmer_TfIdfScorer.tsv"

ESSBM25=$DIR_ESS_TT"output_TextAndTitle_EnglishStemmerStopwords_BM25Scorer.tsv"
ESSCOUN=$DIR_ESS_TT"output_TextAndTitle_EnglishStemmerStopwords_CountScorer.tsv"
ESSTFIF=$DIR_ESS_TT"output_TextAndTitle_EnglishStemmerStopwords_TfIdfScorer.tsv"

GTRU=$DIR_RES"cran_Ground_Truth.tsv"

K="1 3 5 10"

bold=$(tput bold)
normal=$(tput sgr0)

# Bar Chart
echo
echo $bold"########## BarChart ##########"$normal
java com.weird.hw1.plot.BarPlotHWnMDCGResults -K $K -f "$DSBM25" "Default Stemmer" "BM25" "$DSCOUN" "Default Stemmer" "Count" "$DSTFIF" "Default Stemmer" "TfIdf" "$ESBM25" "English Stemmer" "BM25" "$ESCOUN" "English Stemmer" "Count" "$ESTFIF" "English Stemmer" "TfIdf" "$ESSBM25" "English Stopwords Stemmer" "BM25" "$ESSCOUN" "English Stopwords Stemmer" "Count" "$ESSTFIF" "English Stopwords Stemmer" "TfIdf" -g "$GTRU" -o $DIROUT"barChart_All.jpeg"

# Curves
# Default Stemmer 
echo $bold"########## Curves ##########"$normal
echo 
echo $bold"########## Default Stemmer ##########"$normal
java com.weird.hw1.plot.CurvePlotternMDCG -K $K -e "$DSBM25" -g "$GTRU" -o $DIROUT"plot_DefaultStemmer_BM25Scorer.jpeg" -T "Default Stemmer - BM25" -c "blue"
echo
java com.weird.hw1.plot.CurvePlotternMDCG -K $K -e "$DSCOUN" -g "$GTRU" -o $DIROUT"plot_DefaultStemmer_CountScorer.jpeg" -T "Default Stemmer - Count" -c "red"
echo
java com.weird.hw1.plot.CurvePlotternMDCG -K $K -e "$DSTFIF" -g "$GTRU" -o $DIROUT"plot_DefaultStemmer_TfIdfScorer.jpeg" -T "Default Stemmer - TfIdf" -c "green"

# English Stemmer
echo
echo $bold"########## English Stemmer ##########"$normal
java com.weird.hw1.plot.CurvePlotternMDCG -K $K -e "$ESBM25" -g "$GTRU" -o $DIROUT"plot_EnglishStemmer_BM25Scorer.jpeg" -T "English Stemmer - BM25" -c "blue"
echo
java com.weird.hw1.plot.CurvePlotternMDCG -K $K -e "$ESCOUN" -g "$GTRU" -o $DIROUT"plot_EnglishStemmer_CountScorer.jpeg" -T "English Stemmer - Count" -c "red"
echo
java com.weird.hw1.plot.CurvePlotternMDCG -K $K -e "$ESTFIF" -g "$GTRU" -o $DIROUT"plot_EnglishStemmer_TfIdfScorer.jpeg" -T "English Stemmer - TfIdf" -c "green"

# English Stemmer Stopwords
echo
echo $bold"########## English Stemmer with Stopwords ##########"$normal
java com.weird.hw1.plot.CurvePlotternMDCG -K $K -e "$ESSBM25" -g "$GTRU" -o $DIROUT"plot_EnglishStemmerStopwords_BM25Scorer.jpeg" -T "English Stemmer with Stopwords - BM25" -c "blue"
echo
java com.weird.hw1.plot.CurvePlotternMDCG -K $K -e "$ESSCOUN" -g "$GTRU" -o $DIROUT"plot_EnglishStemmerStopwords_CountScorer.jpeg" -T "English Stemmer with Stopwords - Count" -c "red"
echo
java com.weird.hw1.plot.CurvePlotternMDCG -K $K -e "$ESSTFIF" -g "$GTRU" -o $DIROUT"plot_EnglishStemmerStopwords_TfIdfScorer.jpeg" -T "English Stemmer with Stopwords - TfIdf" -c "green"

