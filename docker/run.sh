#!/bin/sh

set -e
set -u

if [ -e $TARGET ]; then
  echo "> Cleaning target folder"
  rm -rf $TARGET/*
fi

echo "> Generate combined file"
sh /moribus/bin/includes.sh |
  xslt -s:- -xsl:/moribus/xslt/combine.xslt -o:$TARGET/all.xml

echo "> Generate API v2 responses"
xslt -s:$TARGET/all.xml -xsl:/moribus/xslt/site-api-v2.xslt -o:$TARGET/site/junk

echo "> Generate web interface"
xslt -s:$TARGET/all.xml -xsl:/moribus/xslt/site-web.xslt -o:$TARGET/site/junk
