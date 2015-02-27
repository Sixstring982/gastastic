#!/bin/bash

srcFolder=res/drawable-xhdpi/

rm -rf res/drawable-hdpi
rm -rf res/drawable-mdpi
mkdir res/drawable-hdpi res/drawable-mdpi

for f in ${srcFolder}*.png
do
    echo "convert $f +repage $f"
    convert $f +repage $f
    echo "convert $f -resize 75% res/drawable-hdpi/${f#$srcFolder}"
    convert $f -resize 75% res/drawable-hdpi/${f#$srcFolder}
    echo "convert $f -resize 50% res/drawable-mdpi/${f#$srcFolder}"
    convert $f -resize 50% res/drawable-mdpi/${f#$srcFolder}
done