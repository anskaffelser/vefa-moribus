<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:mb="urn:fdc:difi.no:2018:vefa:moribus:v2"
                exclude-result-prefixes="xs xsl mb">

    <xsl:output method="html" version="5.0" encoding="UTF-8" indent="yes" />

    <xsl:variable name="domains" select="//mb:Domain[//mb:SubDomain/mb:DomainId = mb:Id]"/>

    <xsl:template match="/mb:Group">
        <xsl:call-template name="home"/>
        <xsl:call-template name="domain"/>
        <xsl:call-template name="subdomain"/>
        <xsl:call-template name="process"/>
        <xsl:call-template name="icd"/>
    </xsl:template>

    <!-- All -->

    <xsl:template name="home">
        <xsl:result-document href="index.html">
            <xsl:variable name="root" select="'.'"/>

            <html lang="en">
                <xsl:call-template name="head">
                    <xsl:with-param name="root" select="$root"/>
                </xsl:call-template>
                <body>
                    <xsl:call-template name="header">
                        <xsl:with-param name="root" select="$root"/>
                    </xsl:call-template>
                    <div class="container-fluid">
                    <div class="row">
                        <xsl:call-template name="sidebar">
                            <xsl:with-param name="root" select="$root"/>
                        </xsl:call-template>
                        <div id="main" class="col-md-9">
                            <h1>Home</h1>
                        </div>
                    </div>
                    </div>
                    <xsl:call-template name="footer"/>
                </body>
            </html>
        </xsl:result-document>
    </xsl:template>

    <!-- Domain -->

    <xsl:template name="domain">
        <xsl:result-document href="domain/index.html">
            <xsl:variable name="root" select="'..'"/>

            <html lang="en">
                <xsl:call-template name="head">
                    <xsl:with-param name="root" select="$root"/>
                </xsl:call-template>
                <body>
                    <xsl:call-template name="header">
                        <xsl:with-param name="root" select="$root"/>
                    </xsl:call-template>

                    <div class="container-fluid">
                    <div class="row">
                        <xsl:call-template name="sidebar">
                            <xsl:with-param name="root" select="$root"/>
                        </xsl:call-template>
                        <div id="main" class="col-md-9">
                            <nav aria-label="breadcrumb">
                                <ol class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="{$root}/">Home</a></li>
                                    <li class="breadcrumb-item active" aria-current="page">Domains</li>
                                </ol>
                            </nav>

                            <h1>Domains</h1>

                            <ul>
                            <xsl:for-each select="//mb:Domain">
                                <li><a href="{$root}/domain/{mb:Alias}"><xsl:value-of select="mb:Title"/></a></li>
                            </xsl:for-each>
                            </ul>
                        </div>
                    </div>
                    </div>
                    <xsl:call-template name="footer"/>
                </body>
            </html>
        </xsl:result-document>
        <xsl:for-each select="$domains">
            <xsl:result-document href="domain/{mb:id(mb:Id)}/index.html">
                <xsl:variable name="root" select="'../..'"/>
                <xsl:variable name="domain" select="current()"/>

                <html lang="en">
                    <xsl:call-template name="head">
                        <xsl:with-param name="root" select="$root"/>
                    </xsl:call-template>
                    <body>
                        <xsl:call-template name="header">
                            <xsl:with-param name="root" select="$root"/>
                        </xsl:call-template>
                        <div class="container-fluid">
                        <div class="row">
                            <xsl:call-template name="sidebar">
                                <xsl:with-param name="root" select="$root"/>
                            </xsl:call-template>
                            <div id="main" class="col-md-9">
                                <nav aria-label="breadcrumb">
                                    <ol class="breadcrumb">
                                        <li class="breadcrumb-item"><a href="{$root}/">Home</a></li>
                                        <li class="breadcrumb-item"><a href="{$root}/domain/">Domains</a></li>
                                        <li class="breadcrumb-item active" aria-current="page"><xsl:value-of select="mb:Title" /></li>
                                    </ol>
                                </nav>

                                <h1><xsl:value-of select="mb:Title"/></h1>

                                <xsl:if test="mb:Description">
                                    <p class="lead"><xsl:value-of select="mb:Description" /></p>
                                </xsl:if>

                                <ul>
                                <xsl:for-each select="//mb:SubDomain[mb:DomainId = $domain/mb:Id]">
                                    <li><a href="{$root}/domain/{mb:id($domain/mb:Id)}/{mb:id(mb:Id)}/"><xsl:value-of select="mb:Title"/></a></li>
                                </xsl:for-each>
                                </ul>

                                <xsl:call-template name="documentation">
                                    <xsl:with-param name="root" select="$root"/>
                                </xsl:call-template>
                            </div>
                        </div>
                        </div>
                        <xsl:call-template name="footer"/>
                    </body>
                </html>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>

    <!-- Subdomain -->

    <xsl:template name="subdomain">
        <xsl:for-each select="mb:SubDomain">
            <xsl:variable name="root" select="'../../..'"/>
            <xsl:variable name="domain" select="//mb:Domain[mb:Id = current()/mb:DomainId][1]"/>
            <xsl:variable name="subdomain" select="current()"/>

            <xsl:result-document href="domain/{mb:id($domain/mb:Id)}/{mb:id(mb:Id)}/index.html">
                <html lang="en">
                    <xsl:call-template name="head">
                        <xsl:with-param name="root" select="$root"/>
                    </xsl:call-template>
                    <body>
                        <xsl:call-template name="header">
                            <xsl:with-param name="root" select="$root"/>
                        </xsl:call-template>
                        <div class="container-fluid">
                        <div class="row">
                            <xsl:call-template name="sidebar">
                                <xsl:with-param name="root" select="$root"/>
                            </xsl:call-template>
                            <div id="main" class="col-md-9">
                                <nav aria-label="breadcrumb">
                                    <ol class="breadcrumb">
                                        <li class="breadcrumb-item"><a href="{$root}/">Home</a></li>
                                        <li class="breadcrumb-item"><a href="{$root}/domain/">Domains</a></li>
                                        <li class="breadcrumb-item"><a href="{$root}/domain/{mb:id($domain/mb:Id)}/"><xsl:value-of select="$domain/mb:Title"/></a></li>
                                        <li class="breadcrumb-item active" aria-current="page"><xsl:value-of select="mb:Title" /></li>
                                    </ol>
                                </nav>

                                <h1><xsl:value-of select="mb:Title"/></h1>

                                <xsl:if test="mb:Description">
                                    <p class="lead"><xsl:value-of select="mb:Description" /></p>
                                </xsl:if>

                                <xsl:if test="//mb:Process[mb:SubDomainId = $subdomain/mb:Id]">
                                    <h3>Processes</h3>

                                    <ul>
                                        <xsl:for-each select="//mb:Process[mb:SubDomainId = $subdomain/mb:Id]">
                                            <xsl:sort select="mb:Title"/>
                                            <li><a href="{$root}/domain/{mb:id($domain/mb:Id)}/{mb:id($subdomain/mb:Id)}/{mb:id(mb:Id)}/"><xsl:value-of select="mb:Title"/></a></li>
                                        </xsl:for-each>
                                    </ul>
                                </xsl:if>

                                <xsl:if test="//mb:Service[mb:SubDomainId = $subdomain/mb:Id]">
                                    <h3>Services</h3>

                                    <ul>
                                        <xsl:for-each select="//mb:Service[mb:SubDomainId = $subdomain/mb:Id]">
                                            <xsl:sort select="mb:Title"/>
                                            <li><a href="{$root}/domain/{mb:id($domain/mb:Id)}/{mb:id($subdomain/mb:Id)}/{mb:id(mb:Id)}/"><xsl:value-of select="mb:Title"/></a></li>
                                        </xsl:for-each>
                                    </ul>
                                </xsl:if>

                                <xsl:if test="mb:IcdId">
                                    <h3>Available ICD</h3>

                                    <p>Processes and services withing this sub-domains may only be used in combination with ICDs during transmission:</p>

                                    <ul>
                                        <xsl:for-each select="mb:IcdId">
                                            <!-- <xsl:sort select="mb:Title"/> -->

                                            <xsl:variable name="icd" select="//mb:Icd[mb:Id = current()]"/>
                                            <li><a href="{$root}/icd/{$icd/mb:Id}/"><xsl:value-of select="$icd/mb:Id"/> - <xsl:value-of select="$icd/mb:Title"/></a></li>
                                        </xsl:for-each>
                                    </ul>
                                </xsl:if>

                                <xsl:call-template name="documentation">
                                    <xsl:with-param name="root" select="$root"/>
                                </xsl:call-template>
                            </div>
                        </div>
                        </div>
                        <xsl:call-template name="footer"/>
                    </body>
                </html>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>

    <!-- Process -->

    <xsl:template name="process">
        <xsl:for-each select="mb:Process">
            <xsl:variable name="root" select="'../../../..'"/>
            <xsl:variable name="process" select="current()"/>
            <xsl:variable name="subdomain" select="//mb:SubDomain[mb:Id = $process/mb:SubDomainId][1]"/>
            <xsl:variable name="domain" select="//mb:Domain[mb:Id = $subdomain/mb:DomainId][1]"/>


            <xsl:result-document href="domain/{mb:id($domain/mb:Id)}/{mb:id($subdomain/mb:Id)}/{mb:id(mb:Id)}/index.html">
                <html lang="en">
                    <xsl:call-template name="head">
                        <xsl:with-param name="root" select="$root"/>
                    </xsl:call-template>
                    <body>
                        <xsl:call-template name="header">
                            <xsl:with-param name="root" select="$root"/>
                        </xsl:call-template>
                        <div class="container-fluid">
                        <div class="row">
                            <xsl:call-template name="sidebar">
                                <xsl:with-param name="root" select="$root"/>
                            </xsl:call-template>
                            <div id="main" class="col-md-9">
                                <nav aria-label="breadcrumb">
                                    <ol class="breadcrumb">
                                        <li class="breadcrumb-item"><a href="{$root}/">Home</a></li>
                                        <li class="breadcrumb-item"><a href="{$root}/domain/">Domains</a></li>
                                        <li class="breadcrumb-item"><a href="{$root}/domain/{mb:id($domain/mb:Id)}/"><xsl:value-of select="$domain/mb:Title"/></a></li>
                                        <li class="breadcrumb-item"><a href="{$root}/domain/{mb:id($domain/mb:Id)}/{mb:id($subdomain/mb:Id)}/"><xsl:value-of select="$subdomain/mb:Title"/></a></li>
                                        <li class="breadcrumb-item active" aria-current="page"><xsl:value-of select="mb:Title" /></li>
                                    </ol>
                                </nav>

                                <h1 class="value"><xsl:value-of select="mb:Title"/></h1>

                                <p class="text-muted"><small class="value"><xsl:value-of select="mb:Id"/></small></p>

                                <xsl:if test="mb:Description">
                                    <p class="lead"><xsl:value-of select="mb:Description" /></p>
                                </xsl:if>

                                <code><small class="value"><xsl:value-of select="mb:Identifier/@scheme" /></small><br/><span class="value"><xsl:value-of select="mb:Identifier"/></span></code>

                                <xsl:for-each select="mb:Role">
                                    <h2 class="value"><xsl:value-of select="mb:Title"/></h2>

                                    <p class="text-muted"><small class="value"><xsl:value-of select="mb:Id"/></small></p>

                                    <dl class="row">
                                        <dt class="col-sm-2">Identifier</dt>
                                        <dd class="col-sm-10 value"><xsl:value-of select="mb:Identifier"/></dd>

                                        <xsl:if test="mb:Encryption">
                                            <dt class="col-sm-2">Encryption</dt>
                                            <dd class="col-sm-10">Minimum "<span class="value"><xsl:value-of select="mb:Encryption/mb:MinimumQuality"/></span>"</dd>
                                        </xsl:if>

                                        <xsl:if test="mb:Signature">
                                            <dt class="col-sm-2">Signature</dt>
                                            <dd class="col-sm-10">Minimum "<span class="value"><xsl:value-of select="mb:Signature/mb:MinimumQuality"/></span>"</dd>
                                        </xsl:if>

                                        <xsl:if test="mb:DocumentType">
                                            <dt class="col-sm-2">Document types</dt>
                                            <dd class="col-sm-10">
                                                <ul>
                                                    <xsl:for-each select="mb:DocumentType">
                                                    <li>
                                                        <span class="value"><xsl:value-of select="mb:Title"/></span>
                                                        <xsl:if test="mb:Encrypted = 'true'">
                                                            <span class="badge badge-dark float-right">Encrypted</span>
                                                          </xsl:if>
                                                        <xsl:if test="mb:Signed = 'true'">
                                                            <span class="badge badge-dark float-right">Signed</span>
                                                          </xsl:if>

                                                        <xsl:if test="mb:Identifier">
                                                            <code><small class="value"><xsl:value-of select="mb:Identifier/@scheme"/></small><br /><span class="value"><xsl:value-of select="mb:Identifier"/></span></code>
                                                        </xsl:if>
                                                    </li>
                                                    </xsl:for-each>
                                                </ul>
                                            </dd>
                                        </xsl:if>
                                    </dl>
                                </xsl:for-each>

                                <xsl:call-template name="documentation">
                                    <xsl:with-param name="root" select="$root"/>
                                </xsl:call-template>
                            </div>
                        </div>
                        </div>
                        <xsl:call-template name="footer"/>
                    </body>
                </html>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>

    <!-- ICD -->

    <xsl:template name="icd">
        <xsl:result-document href="icd/index.html">
            <xsl:variable name="root" select="'..'"/>

            <html lang="en">
                <xsl:call-template name="head">
                    <xsl:with-param name="root" select="$root"/>
                </xsl:call-template>
                <body>
                    <xsl:call-template name="header">
                        <xsl:with-param name="root" select="$root"/>
                    </xsl:call-template>
                    <div class="container-fluid">
                    <div class="row">
                        <xsl:call-template name="sidebar">
                            <xsl:with-param name="root" select="$root"/>
                        </xsl:call-template>
                        <div id="main" class="col-md-9">

                            <nav aria-label="breadcrumb">
                                <ol class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="{$root}/">Home</a></li>
                                    <li class="breadcrumb-item active" aria-current="page">ICD</li>
                                </ol>
                            </nav>

                            <h1>Internation Code Designators (ICD)</h1>

                            <ul>
                                <xsl:for-each select="//mb:Icd">
                                    <xsl:sort select="mb:Id"/>
                                    <li><a href="{$root}/icd/{mb:Id}/"><xsl:value-of select="mb:Id"/> - <xsl:value-of select="mb:Title"/></a></li>
                                </xsl:for-each>
                            </ul>
                        </div>
                    </div>
                    </div>
                    <xsl:call-template name="footer"/>
                </body>
            </html>
        </xsl:result-document>
        <xsl:for-each select="mb:Icd">
            <xsl:result-document href="icd/{mb:id(mb:Id)}/index.html">
                <xsl:variable name="root" select="'../..'"/>

                <html lang="en">
                    <xsl:call-template name="head">
                        <xsl:with-param name="root" select="$root"/>
                    </xsl:call-template>
                    <body>
                        <xsl:call-template name="header">
                            <xsl:with-param name="root" select="$root"/>
                        </xsl:call-template>
                        <div class="container-fluid">
                        <div class="row">
                            <xsl:call-template name="sidebar">
                                <xsl:with-param name="root" select="$root"/>
                            </xsl:call-template>
                            <div id="main" class="col-md-9">

                                <nav aria-label="breadcrumb">
                                    <ol class="breadcrumb">
                                        <li class="breadcrumb-item"><a href="{$root}/">Home</a></li>
                                        <li class="breadcrumb-item"><a href="{$root}/tp/">ICD</a></li>
                                        <li class="breadcrumb-item active" aria-current="page"><xsl:value-of select="mb:Id" /></li>
                                    </ol>
                                </nav>

                                <h1><xsl:value-of select="mb:Id"/></h1>

                                <xsl:if test="mb:Description">
                                    <p class="lead"><xsl:value-of select="mb:Description" /></p>
                                </xsl:if>

                                <xsl:call-template name="documentation">
                                    <xsl:with-param name="root" select="$root"/>
                                </xsl:call-template>
                            </div>
                        </div>
                        </div>
                        <xsl:call-template name="footer"/>
                    </body>
                </html>
            </xsl:result-document>
        </xsl:for-each>
    </xsl:template>

    <!-- Partials -->

    <xsl:template name="head">
        <xsl:param name="root"/>

        <head>
            <meta charset="utf-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>

            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous"/>
            <style>
                code {
                    display: block;
                    margin: 5pt 0;
                    padding: 5pt;
                    background-color: #ddd;
                    color: #333;
                    word-wrap: break-word;
                }
                code small {
                    font-style: italic;
                }
                .value {
                  cursor: pointer;
                }
            </style>

            <title>Moribus</title>
        </head>
    </xsl:template>

    <xsl:template name="header">
        <xsl:param name="root"/>

        <nav class="navbar navbar-light bg-light shadow" style="margin-bottom: 20pt;">
            <a class="navbar-brand" href="{$root}/">Moribus</a>
        </nav>
    </xsl:template>

    <xsl:template name="sidebar">
        <xsl:param name="root"/>

        <div id="sidebar" class="col-md-2">
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a href="{$root}/domain/">Domains</a>

                    <ul class="flex-column">
                        <xsl:for-each select="$domains">
                            <li class="nav-item">
                                <a href="{$root}/domain/{mb:id(mb:Id)}/"><xsl:value-of select="mb:Title"/></a>
                            </li>
                        </xsl:for-each>
                    </ul>
                </li>
                <li class="nav-item">
                    <a href="{$root}/icd/">ICD</a>
                </li>
            </ul>
        </div>
    </xsl:template>

    <xsl:template name="footer">
        <div id="footer">

        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.4/clipboard.min.js"></script>
        <script>
          new ClipboardJS('.value', {
            target: function(trigger) {
              return trigger;
            }
          });
        </script>
    </xsl:template>

    <xsl:template name="documentation">
        <xsl:param name="root"/>

        <xsl:if test="mb:Documentation">
            <h2>Documentation</h2>

            <div class="list-group">
                <a href="{mb:Address}" class="list-group-item list-group-item-action flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1"><xsl:value-of select="mb:Title" /></h5>
                    </div>
                    <xsl:if test="mb:Description">
                        <p class="mb-1"><xsl:value-of select="mb:Description"/></p>
                    </xsl:if>
                </a>
            </div>
        </xsl:if>
    </xsl:template>

    <!-- Functions -->
    <xsl:function name="mb:id">
        <xsl:param name="current"/>

        <xsl:choose>
            <xsl:when test="$current/../mb:Alias">
                <xsl:value-of select="$current/../mb:Alias"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$current"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

</xsl:stylesheet>
