javac -d bin -classpath ".:./lib/fr.lip6.pnml.framework.3rdpartimports_2.2.14.jar\
:./lib/fr.lip6.pnml.framework.ptnet_2.2.14.jar\
:./lib/fr.lip6.pnml.framework.utils_2.2.14.jar\
:./lib/org.eclipse.emf.common_2.17.0.v20190920-0401.jar\
:./lib/org.eclipse.emf.ecore_2.20.0.v20190920-0401.jar\
:./lib/org.slf4j.api_1.7.30.v20200204-2150.jar" ./src/**/*.java

echo "Usage: ./leader_os nb_processus (for normal net)
Or: ./leader_os nb_processus a (for abstracted net)"
