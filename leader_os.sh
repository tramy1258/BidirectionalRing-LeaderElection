if [ $# -lt 1 ]
then 
echo "Usage: ./leader_os nb_processus (for normal net)
Or: ./leader_os nb_processus a (for abstracted net)";

elif [ $1 -eq 0 ]
then
echo "Must have at least one process";

else
	if [ $# -eq 1 ]
	then
	java -classpath "./bin/:\
./lib/fr.lip6.pnml.framework.3rdpartimports_2.2.14.jar:\
./lib/fr.lip6.pnml.framework.ptnet_2.2.14.jar:\
./lib/fr.lip6.pnml.framework.utils_2.2.14.jar:\
./lib/org.eclipse.emf.common_2.17.0.v20190920-0401.jar:\
./lib/org.eclipse.emf.ecore_2.20.0.v20190920-0401.jar:\
./lib/org.slf4j.api_1.7.30.v20200204-2150.jar:\
./lib/slf4j-jdk14-2.0.0-alpha1.jar" ringleader.Test $1;
	else
	java -classpath "./bin/:\
./lib/fr.lip6.pnml.framework.3rdpartimports_2.2.14.jar:\
./lib/fr.lip6.pnml.framework.ptnet_2.2.14.jar:\
./lib/fr.lip6.pnml.framework.utils_2.2.14.jar:\
./lib/org.eclipse.emf.common_2.17.0.v20190920-0401.jar:\
./lib/org.eclipse.emf.ecore_2.20.0.v20190920-0401.jar:\
./lib/org.slf4j.api_1.7.30.v20200204-2150.jar:\
./lib/slf4j-jdk14-2.0.0-alpha1.jar" ringleader.Test $1 $2;
	fi
fi
