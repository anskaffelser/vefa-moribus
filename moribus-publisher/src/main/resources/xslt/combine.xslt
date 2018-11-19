<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:mb="urn:fdc:difi.no:2018:vefa:moribus:v2"
                xmlns="urn:fdc:difi.no:2018:vefa:moribus:v2"
                exclude-result-prefixes="xs xsl mb">

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/mb:Group">
        <Group>
            <xsl:apply-templates/>
        </Group>
    </xsl:template>

    <xsl:template match="mb:Include">
        <xsl:apply-templates select="document(text())" mode="include"/>
    </xsl:template>

    <xsl:template match="*" mode="include">
        <xsl:choose>
            <xsl:when test="local-name() = 'Group'">
                <xsl:apply-templates select="*"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="current()"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="comment()"/>

    <xsl:template match="@*|element()|text()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>