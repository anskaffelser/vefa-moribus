#!/bin/sh

set -e
set -u

echo "<Group xmlns=\"urn:fdc:difi.no:2018:vefa:moribus:v2\">"

for file in $(find $SOURCE -type f -name *.xml | sort); do
  echo "<Include>$file</Include>"
done

echo "</Group>"
