#!/bin/bash
echo "Ontology Selected: $1"
if [ $1 == "hp" ]
then
  wget -P data/ --no-use-server-timestamps https://purl.obolibrary.org/obo/hp/hp-base.json
  wget -P data/ --no-use-server-timestamps https://purl.obolibrary.org/obo/hp/translations/hp-all.babelon.tsv
elif [ $1 == "mp" ]
then
  wget -P data/ --no-use-server-timestamps https://purl.obolibrary.org/obo/mp/mp-base.json
elif [ $1 == "mondo"]
then
  wget -P data/ --no-use-server-timestamps https://purl.obolibrary.org/obo/mondo/mondo-base.json
elif [ $1 == "maxo"]
then
 wget -P data/ --no-use-server-timestamps https://purl.obolibrary.org/obo/maxo/maxo-base.json
fi
