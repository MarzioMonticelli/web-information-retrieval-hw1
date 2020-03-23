### scoring ###
if [[ $# -eq 1 ]]; then
	MD=$1
elif [[ $# -eq 2 ]]; then
	MD=$1
	GTRU=$2
else 
 	MD="./QueriesResults"
	GTRU="./Queries/cran_Ground_Truth.tsv"
fi


bold=$(tput bold)
normal=$(tput sgr0)

K=(1 3 5 10 0);

echo ${bold}"############### AverageRPRecision ###############"
echo "Default Stemmer - BM25Scorer"${normal}
java com.weird.hw1.ese.AverageRPrecision $MD"/DefaultStemmer/DSTextAndTitle/output_TextAndTitle_DefaultStemmer_BM25Scorer.tsv" $GTRU

echo
echo ${bold}"English Stemmer - BM25Scorer"${normal}
java com.weird.hw1.ese.AverageRPrecision $MD"/EnglishStemmer/ESTextAndTitle/output_TextAndTitle_EnglishStemmer_BM25Scorer.tsv" $GTRU

echo
echo ${bold}"English Stopwords Stemmer - BM25Scorer"${normal}
java com.weird.hw1.ese.AverageRPrecision $MD"/EnglishStemmerStopwords/ESSTextAndTitle/output_TextAndTitle_EnglishStemmerStopwords_BM25Scorer.tsv" $GTRU

echo
echo ${bold}"Default Stemmer - CountScorer"${normal}
java com.weird.hw1.ese.AverageRPrecision $MD"/DefaultStemmer/DSTextAndTitle/output_TextAndTitle_DefaultStemmer_CountScorer.tsv" $GTRU

echo
echo ${bold}"English Stemmer - CountScorer"${normal}
java com.weird.hw1.ese.AverageRPrecision $MD"/EnglishStemmer/ESTextAndTitle/output_TextAndTitle_EnglishStemmer_CountScorer.tsv" $GTRU

echo
echo ${bold}"English Stopwords Stemmer - CountScorer"${normal}
java com.weird.hw1.ese.AverageRPrecision $MD"/EnglishStemmerStopwords/ESSTextAndTitle/output_TextAndTitle_EnglishStemmerStopwords_CountScorer.tsv" $GTRU

echo
echo ${bold}"Default Stemmer - TfIdfScorer"${normal}
java com.weird.hw1.ese.AverageRPrecision $MD"/DefaultStemmer/DSTextAndTitle/output_TextAndTitle_DefaultStemmer_TfIdfScorer.tsv" $GTRU

echo
echo ${bold}"English Stemmer - TfIdfScorer"${normal}
java com.weird.hw1.ese.AverageRPrecision $MD"/EnglishStemmer/ESTextAndTitle/output_TextAndTitle_EnglishStemmer_TfIdfScorer.tsv" $GTRU

echo
echo ${bold}"English Stopwords Stemmer - TfIdfScorer"${normal}
java com.weird.hw1.ese.AverageRPrecision $MD"/EnglishStemmerStopwords/ESSTextAndTitle/output_TextAndTitle_EnglishStemmerStopwords_TfIdfScorer.tsv" $GTRU

echo
echo ${bold}"############### nMDCG ###############"
echo "Default Stemmer - BM25Scorer"${normal}
for k in ${K[*]}; do 
	java com.weird.hw1.ese.nMDCG -k $k $MD"/DefaultStemmer/DSTextAndTitle/output_TextAndTitle_DefaultStemmer_BM25Scorer.tsv" $GTRU
done

echo
echo ${bold}"English Stemmer - BM25Scorer"${normal}
for k in ${K[*]}; do 
	java com.weird.hw1.ese.nMDCG -k $k $MD"/EnglishStemmer/ESTextAndTitle/output_TextAndTitle_EnglishStemmer_BM25Scorer.tsv" $GTRU
done

echo
echo ${bold}"English Stopwords Stemmer - BM25Scorer"${normal}
for k in ${K[*]}; do 
	java com.weird.hw1.ese.nMDCG -k $k $MD"/EnglishStemmerStopwords/ESSTextAndTitle/output_TextAndTitle_EnglishStemmerStopwords_BM25Scorer.tsv" $GTRU
done

echo
echo ${bold}"Default Stemmer - CountScorer"${normal}
for k in ${K[*]}; do 
java com.weird.hw1.ese.nMDCG -k $k $MD"/DefaultStemmer/DSTextAndTitle/output_TextAndTitle_DefaultStemmer_CountScorer.tsv" $GTRU
done

echo
echo ${bold}"English Stemmer - CountScorer"${normal}
for k in ${K[*]}; do 
	java com.weird.hw1.ese.nMDCG -k $k $MD"/EnglishStemmer/ESTextAndTitle/output_TextAndTitle_EnglishStemmer_CountScorer.tsv" $GTRU
done

echo
echo ${bold}"English Stopwords Stemmer - CountScorer"${normal}
for k in ${K[*]}; do 
	java com.weird.hw1.ese.nMDCG -k $k $MD"/EnglishStemmerStopwords/ESSTextAndTitle/output_TextAndTitle_EnglishStemmerStopwords_CountScorer.tsv" $GTRU
done

echo
echo ${bold}"Default Stemmer - TfIdfScorer"${normal}
for k in ${K[*]}; do 
	java com.weird.hw1.ese.nMDCG -k $k $MD"/DefaultStemmer/DSTextAndTitle/output_TextAndTitle_DefaultStemmer_TfIdfScorer.tsv" $GTRU
done

echo
echo ${bold}"English Stemmer - TfIdfScorer"${normal}
for k in ${K[*]}; do 
	java com.weird.hw1.ese.nMDCG -k $k $MD"/EnglishStemmer/ESTextAndTitle/output_TextAndTitle_EnglishStemmer_TfIdfScorer.tsv" $GTRU
done

echo
echo ${bold}"English Stopwords Stemmer - TfIdfScorer"${normal}
for k in ${K[*]}; do 
	java com.weird.hw1.ese.nMDCG -k $k $MD"/EnglishStemmerStopwords/ESSTextAndTitle/output_TextAndTitle_EnglishStemmerStopwords_TfIdfScorer.tsv" $GTRU
done
