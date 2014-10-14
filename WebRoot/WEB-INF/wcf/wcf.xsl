<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" indent="no" encoding="ISO-8859-1"/>

<xsl:param name="context"/>
<xsl:param name="renderId"/>

<xsl:include href="catedit.xsl"/>
<xsl:include href="changeorder.xsl"/>
<xsl:include href="controls.xsl"/>
<xsl:include href="xform.xsl"/>
<xsl:include href="xtable.xsl"/>
<xsl:include href="xtree.xsl"/>

<xsl:template match="skip">
  <xsl:apply-templates/>
</xsl:template>


<!--
The global stylesheet parameters border and renderId may
be overwritten by an attribute. The following templates
return the value to use, either the attribute if present
or the global parameter.

This is used when a component contains a nested component
that requires its own border/renderId paramerters.
-->

<xsl:template name="select-renderId">
  <xsl:choose>
    <xsl:when test="@renderId">
      <xsl:value-of select="@renderId"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$renderId"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="select-border">
  <xsl:choose>
    <xsl:when test="@border">
      <xsl:value-of select="@border"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$border"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<!-- identity transform -->
<xsl:template match="*|@*|node()">
  <xsl:copy>
    <xsl:apply-templates select="*|@*|node()"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>