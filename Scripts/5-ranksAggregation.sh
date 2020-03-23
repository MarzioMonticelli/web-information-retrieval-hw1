DIR="./"
DIR_ESS=$DIR"QueriesResults/EnglishStemmerStopwords/"
DIR_ESS_Te=$DIR_ESS"ESSText/"
DIR_ESS_Ti=$DIR_ESS"ESSTitle/"

F_ESS_Te=$DIR_ESS_Te"output_text_EnglishStemmerStopwords_BM25Scorer.tsv"
F_ESS_Ti=$DIR_ESS_Ti"output_title_EnglishStemmerStopwords_BM25Scorer.tsv"
F_ESS_ATHR=$DIR_ESS"output_aggregated_Threshold_EnglishStemmerStopwords_BM25Scorer.tsv"
F_ESS_AVAG=$DIR_ESS"output_aggregated_Fagin_EnglishStemmerStopwords_BM25Scorer.tsv"

GTRU="./Queries/cran_Ground_Truth.tsv"
R="-r 2"
K=""
V=""

while [[ $# -gt 1 ]]
do
	key="$1"

	case $key in
		-k|-K)
		K="-k "$2
		shift
		;;
		-R|-r|--ratio)
		R="-r "$2
		shift
		;;
		-t|--text)
		F_ESS_Te=$2
		shift
		;;
		-T|--title)
		F_ESS_Ti=$2
		shift
		;;
		-o|--output)
		F_ESS_A=$2
		shift
		;;
		-v|--verbose)
			V=--verbose
			;;
		-V|--Vverbose)
			V=--Vverbose
			;;
		*)
		"usage: "$BASH_SOURCE" [-k|-K K_VALUE -R|--ratio RATIO -t|--text Text_score_FILE -T|--title Title_score_FILE -o|--output Output_File]"
		exit 1
		;;
	esac
	shift
done

echo
# Fagin's Algorithm
java com.weird.hw1.rank_aggregation.FaginsAlgorithm $K $R $F_ESS_Te $F_ESS_Ti $F_ESS_AVAG $V

echo -ne $bold"Fagin's Algorithm"$normal" " 
java com.weird.hw1.ese.AverageRPrecision $F_ESS_AVAG $GTRU

# Fagin's Threshold Algorithm
java com.weird.hw1.rank_aggregation.ThresholdAlgorithm2 $K $R $F_ESS_Te $F_ESS_Ti $F_ESS_ATHR $V

echo -ne $bold"Fagin's Threshold Algorithm"$normal" "
java com.weird.hw1.ese.AverageRPrecision $F_ESS_ATHR $GTRU


