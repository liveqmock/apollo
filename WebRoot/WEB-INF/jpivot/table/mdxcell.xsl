<?xml version="1.0"?>

<!-- renders a "table" with one single cell to a <span>value</span> -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- the id of the table for httpUnit -->
<xsl:param name="renderId"/>
<xsl:param name="context"/>
<xsl:param name="imgpath" select="'jpivot/table'"/>

<xsl:output method="html" indent="yes"/>

<xsl:template match="mdxtable">
  <xsl:if test="@message">
    <div class="table-message"><xsl:value-of select="@message"/></div>
  </xsl:if>
  <xsl:apply-templates select="body/row/cell"/>
</xsl:template>

<xsl:template match="cell">
  <span nowrap="nowrap" class="cell-{@style}">
    <xsl:call-template name="render-label">
      <xsl:with-param name="label">
        <xsl:value-of select="@value"/>
      </xsl:with-param>
    </xsl:call-template>
  </span>
</xsl:template>


<xsl:template name="render-label">
  <xsl:param name="label"/>
  <xsl:choose>
    <xsl:when test="property[@name='link']">
      <a href="{property[@name='link']/@value}" target="_blank">
        <xsl:value-of select="$label"/>
        <xsl:apply-templates select="property"/>
      </a>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$label"/>
      <xsl:apply-templates select="property"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="property[@name='arrow']">
  <span style="margin-left: 0.5ex">
    <img border="0" src="{$context}/{$imgpath}/arrow-{@value}.gif" width="10" height="10"/>
  </span>
</xsl:template>

<xsl:template match="property[@name='image']">
  <span style="margin-left: 0.5ex">
    <xsl:choose>
      <xsl:when test="starts-with(@value, '/')">
        <img border="0" src="{$context}{@value}"/>
      </xsl:when>
      <xsl:otherwise>
        <img border="0" src="{@value}"/>
      </xsl:otherwise>
    </xsl:choose>
  </span>
</xsl:template>

<xsl:template match="property[@name='cyberfilter']">
  <span style="margin-left: 0.5ex">
    <img align="middle" src="{$context}/{$imgpath}/filter-{@value}.gif" width="51" height="12"/>
  </span>
</xsl:template>

<xsl:template match="property"/>

</xsl:stylesheet>