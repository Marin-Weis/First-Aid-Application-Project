/* Commande pour compiler l'appli */
mkdir -p ../class && javac -d ../class/ --module-path ../lib/javafx --add-modules javafx.controls,javafx.fxml -cp ../lib/mysql/mysql-connector-j-9.3.0.jar ../src/controller/*.java ../src/metier/persistence/*.java ../src/metier/service/*.java ../src/metier/graphe/algorithme/*.java ../src/metier/graphe/model/*.java ../src/metier/graphe/model/dao/*.java ../src/vue/*.java && mkdir -p ../class/ressources/{fxml,css,img,fonts} && cp ../src/ressources/fxml/*.fxml ../class/ressources/fxml/ && cp ../src/ressources/css/*.css ../class/ressources/css/ && cp ../src/ressources/img/* ../class/ressources/img/ && cp ../src/ressources/fonts/*.otf ../class/ressources/fonts/

/* Commande pour lancer l'appli */
java --module-path ../lib/javafx --add-modules javafx.controls,javafx.fxml -cp ../class:../lib/mysql/mysql-connector-j-9.3.0.jar vue.SecouristeView
