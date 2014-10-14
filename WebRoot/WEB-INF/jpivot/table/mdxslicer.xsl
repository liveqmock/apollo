<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

<xsl:output method="html" indent="yes"/>

<!-- xsl:param name="testparam" select="'default-value'"/ -->

<xsl:template match="/mdxtable">
  <!-- h2><xsl:value-of select="$testparam"/></h2 -->
  <xsl:apply-templates select="slicer/member"/>
</xsl:template>

<xsl:template match="member">
  <xsl:if test="preceding-sibling::member">
    <xsl:text>, </xsl:text>
  </xsl:if>
  <xsl:value-of select="@level"/>
  <xsl:text>=</xsl:text>
  <xsl:value-of select="@caption"/>
</xsl:template>


</xsl:stylesheet>
