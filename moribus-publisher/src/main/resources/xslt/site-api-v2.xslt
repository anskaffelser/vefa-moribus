<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:mb="urn:fdc:difi.no:2018:vefa:moribus:v2"
                xmlns:f="urn:fdc:klakegg.net:2018:xml:fileextractor:Files-1"
                xmlns="urn:fdc:difi.no:2018:vefa:moribus:v2"
                exclude-result-prefixes="xs xsl mb">

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/mb:Group">
        <f:Files>
            <xsl:call-template name="all"/>
            <xsl:call-template name="domain"/>
            <xsl:call-template name="subdomain"/>
            <xsl:call-template name="process"/>
            <xsl:call-template name="service"/>
            <xsl:call-template name="icd"/>
            <xsl:call-template name="tp"/>
            <xsl:call-template name="encrypted"/>
        </f:Files>
    </xsl:template>

    <!-- All -->

    <xsl:template name="all">
        <f:File filename="api/v2/all.xml">
            <Group>
                <xsl:copy-of select="//mb:Domain"/>
                <xsl:copy-of select="//mb:SubDomain"/>
                <xsl:copy-of select="//mb:Process"/>
                <xsl:copy-of select="//mb:Service"/>
                <xsl:copy-of select="//mb:Icd"/>
                <xsl:copy-of select="//mb:TransportProfile"/>
            </Group>
        </f:File>
    </xsl:template>

    <!-- Domain -->

    <xsl:template name="domain">
        <f:File filename="api/v2/domain/all.xml">
            <Group>
                <xsl:copy-of select="//mb:Domain"/>
            </Group>
        </f:File>
        <xsl:for-each select="//mb:Domain">
            <f:File filename="api/v2/domain/{mb:Id}.xml">
                <xsl:copy-of select="current()"/>
            </f:File>
        </xsl:for-each>
    </xsl:template>

    <!-- Subdomain -->

    <xsl:template name="subdomain">
        <f:File filename="api/v2/subdomain/all.xml">
            <Group>
                <xsl:copy-of select="//mb:SubDomain"/>
            </Group>
        </f:File>
        <xsl:for-each select="//mb:SubDomain">
            <f:File filename="api/v2/subdomain/{mb:Id}.xml">
                <xsl:copy-of select="current()"/>
            </f:File>
        </xsl:for-each>
    </xsl:template>

    <!-- Process -->

    <xsl:template name="process">
        <f:File filename="api/v2/process/all.xml">
            <Group>
                <xsl:copy-of select="//mb:Process"/>
            </Group>
        </f:File>
        <xsl:for-each select="//mb:Process">
            <f:File filename="api/v2/process/{mb:Id}.xml">
                <xsl:copy-of select="current()"/>
            </f:File>
        </xsl:for-each>
    </xsl:template>

    <!-- Process -->

    <xsl:template name="service">
        <f:File filename="api/v2/service/all.xml">
            <Group>
                <xsl:copy-of select="//mb:Service"/>
            </Group>
        </f:File>
        <xsl:for-each select="//mb:Service">
            <f:File filename="api/v2/service/{mb:Id}.xml">
                <xsl:copy-of select="current()"/>
            </f:File>
        </xsl:for-each>
    </xsl:template>

    <!-- Process -->

    <xsl:template name="icd">
        <f:File filename="api/v2/icd/all.xml">
            <Group>
                <xsl:copy-of select="//mb:Icd"/>
            </Group>
        </f:File>
        <xsl:for-each select="//mb:Icd">
            <f:File filename="api/v2/icd/{mb:Id}.xml">
                <xsl:copy-of select="current()"/>
            </f:File>
        </xsl:for-each>
    </xsl:template>

    <!-- Process -->

    <xsl:template name="tp">
        <f:File filename="api/v2/tp/all.xml">
            <Group>
                <xsl:copy-of select="//mb:TransportProfile"/>
            </Group>
        </f:File>
        <xsl:for-each select="//mb:TransportProfile">
            <f:File filename="api/v2/tp/{mb:Id}.xml">
                <xsl:copy-of select="current()"/>
            </f:File>
        </xsl:for-each>
    </xsl:template>

    <!-- Encrypted -->

    <xsl:template name="encrypted">
        <f:File filename="api/v2/encrypted.xml">
            <Group>
                <xsl:variable name="processes" select="//mb:Process[mb:Role/mb:Encryption]"/>
                <xsl:variable name="subdomains" select="for $i in distinct-values($processes/mb:SubDomainId) return //mb:SubDomain[mb:Id = $i]"/>
                <xsl:variable name="domains" select="for $i in distinct-values($subdomains/mb:DomainId) return //mb:Domain[mb:Id = $i]"/>

                <xsl:copy-of select="$domains"/>
                <xsl:copy-of select="$subdomains"/>
                <xsl:copy-of select="$processes"/>
                <xsl:copy-of select="//mb:Icd"/>
            </Group>
        </f:File>
    </xsl:template>

</xsl:stylesheet>