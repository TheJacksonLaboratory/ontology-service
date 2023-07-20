#!/bin/bash
echo "Ontology Selected: $1"
if [ $1 == "hp" ]
then
  wget -P data/ --no-use-server-timestamps https://purl.obolibrary.org/obo/hp/hp-simple-non-classified.json
  wget -P data/ --no-use-server-timestamps https://purl.obolibrary.org/obo/hp/translations/hp-all.babelon.tsv
elif [ $1 == "mp" ]
then
  wget -P data/ --no-use-server-timestamps https://purl.obolibrary.org/obo/mp/mp-simple-non-classified.json
fi