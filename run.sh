#!/bin/bash

echo "========================================"
echo "  Gestion des Factures - Micro-Entrepreneur"
echo "========================================"
echo

echo "Compilation du projet..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "Erreur lors de la compilation !"
    exit 1
fi

echo
echo "Lancement de l'application..."
mvn javafx:run

if [ $? -ne 0 ]; then
    echo "Erreur lors du lancement !"
    exit 1
fi 