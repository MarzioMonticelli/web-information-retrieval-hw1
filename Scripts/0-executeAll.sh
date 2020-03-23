K_DEF=5
re='^[0-9]+$'
V=""

NUM_SCRIPTS=6


bold=$(tput bold)
normal=$(tput sgr0)

# Preparsing Help
if [[ $# -ge 1 ]]; then
	for arg in $@; do
		if [[ $arg == "--help" || $arg == "-h" ]]; then
			me=$BASH_SOURCE
			echo "$ source" $me":" 
			echo "	-h	--help		prints this help"
			echo "	-v	--verbose	sets the verbose mode"
		fi
	done
fi

# Handle verbose mode
if [[ $# -ne 1 || ( $1 != "-v"  && $1 != "--verbose" ) ]]; then
	V=" > /dev/null"
	echo ${bold}"[1/"$NUM_SCRIPTS"]"${normal}" Performing collection creation"
fi

eval source 1-createCollection.sh $V

if [[ $V != "" ]]; then
	echo ${bold}"[2/"$NUM_SCRIPTS"]"${normal}" Performing Indexes creation"
fi
eval source 2-createIndexes.sh $V

if [[ $V != "" ]]; then
	echo ${bold}"[3/"$NUM_SCRIPTS"]"${normal}" Obtaining Results"
fi

eval source 3-obtainResults.sh $V

echo
echo ${bold}"[4/"$NUM_SCRIPTS"]"${normal}" Press Enter to perform scoring"
read
clear

source 4-performScoring.sh

# grub K Value
echo
echo ${bold}"[5/"$NUM_SCRIPTS"]"${normal}" Rank Aggreagation"
while (true); do
	echo -n "which value for K do you want to use? [Press ENTER  to use the max possible K value for each instance] "
	read K
	if [[ $K =~ $re ]]; then
		K=-k $K
		break;
	elif [[ $K == "" ]]; then
		K=""
		break;
	else
		echo "[ERR] \""$K"\" is not a number. Retry" >&2;
	fi
done

# grub Ratio value
while (true); do
	echo -n "which value for Ratio (importance of Title wrt Text) do you want to use? [Default=2] "
	read R
	if [[ $R =~ $re ]]; then
		R=-r $R
		break;
	elif [[ $R == "" ]]; then
		R=""
		break;
	else
		echo "[ERR] \""$R"\" is not a number. Retry" >&2;
	fi
done

eval source 5-ranksAggregation.sh $K $R

echo
echo ${bold}"[6/"$NUM_SCRIPTS"]"${normal}" Plot Results"
echo "Press ENTER to plot the results"
read

eval source 6-plot.sh

