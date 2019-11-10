<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:mb="urn:fdc:difi.no:2018:vefa:moribus:v2"
                xmlns="urn:fdc:difi.no:2018:vefa:moribus:v2"
                exclude-result-prefixes="xs xsl mb">

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/mb:Group">
        <xsl:call-template name="all"/>
        <xsl:call-template name="domain"/>
        <xsl:call-template name="subdomain"/>
        <xsl:call-template name="process"/>
        <xsl:call-template name="service"/>
        <xsl:call-template name="icd"/>
        <xsl:call-template name="encrypted"/>
    </xsl:template>

    <!-- All -->

    <xsl:template name="all">
        <xsl:result-document href="api/v2/all.xml">
            <Group>
                <xsl:copy-of select="//mb:Domain"/>
                <xsl:copy-of select="//mb:SubDomain"/>
                <xsl:copy-of select="//mb:Process"/>
                <xsl:copy-of select="//mb:Service"/>
                <xsl:copy-of select="//mb:Icd"/>
                <xsl:copy-of select="//mb:TransportProfile"/>
            </Group>
        </xsl:result-document>
    </xsl:template>

    <!-- Domain -->

    <xsl:template name="domain">
        <xsl:result-document href="api/v2/domain/all.xml">
            <Group>
                <xsl:copy-of select="//mb:Domain"/>
            </Group>
        </xsl:result-document>
        <xsl:for-each select="//mb:Domain">
            <xsl:result-document href="api/v2/domain/{mb:Id}.xml">
                <xsl:copy-of select="current()"/>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>

    <!-- Subdomain -->

    <xsl:template name="subdomain">
        <xsl:result-document href="api/v2/subdomain/all.xml">
            <Group>
                <xsl:copy-of select="//mb:SubDomain"/>
            </Group>
        </xsl:result-document>
        <xsl:for-each select="//mb:SubDomain">
            <xsl:result-document href="api/v2/subdomain/{mb:Id}.xml">
                <xsl:copy-of select="current()"/>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>

    <!-- Process -->

    <xsl:template name="process">
        <xsl:result-document href="api/v2/process/all.xml">
            <Group>
                <xsl:copy-of select="//mb:Process"/>
            </Group>
        </xsl:result-document>
        <xsl:for-each select="//mb:Process">
            <xsl:result-document href="api/v2/process/{mb:Id}.xml">
                <xsl:copy-of select="current()"/>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>

    <!-- Process -->

    <xsl:template name="service">
        <xsl:result-document href="api/v2/service/all.xml">
            <Group>
                <xsl:copy-of select="//mb:Service"/>
            </Group>
        </xsl:result-document>
        <xsl:for-each select="//mb:Service">
            <xsl:result-document href="api/v2/service/{mb:Id}.xml">
                <xsl:copy-of select="current()"/>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>

    <!-- Process -->

    <xsl:template name="icd">
        <xsl:result-document href="api/v2/icd/all.xml">
            <Group>
                <xsl:copy-of select="//mb:Icd"/>
            </Group>
        </xsl:result-document>
        <xsl:for-each select="//mb:Icd">
            <xsl:result-document href="api/v2/icd/{mb:Id}.xml">
                <xsl:copy-of select="current()"/>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>

    <!-- CertPub -->

    <xsl:template name="encrypted">
        <xsl:result-document href="api/v2/encrypted.xml">
            <Group>
                <xsl:variable name="processes" select="//mb:Process[mb:Role/mb:Encryption | mb:Role/mb:Signature]"/>
                <xsl:variable name="subdomains" select="for $i in distinct-values($processes/mb:SubDomainId) return //mb:SubDomain[mb:Id = $i]"/>
                <xsl:variable name="domains" select="for $i in distinct-values($subdomains/mb:DomainId) return //mb:Domain[mb:Id = $i]"/>

                <xsl:copy-of select="$domains"/>
                <xsl:copy-of select="$subdomains"/>
                <xsl:copy-of select="$processes"/>
                <xsl:copy-of select="//mb:Icd"/>
            </Group>
        </xsl:result-document>
    </xsl:template>

</xsl:stylesheet>
