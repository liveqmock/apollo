<?xml version="1.0" encoding="iso-8859-1"?>


<!--
stylesheet for wcf table component
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  version="1.0">


<xsl:param name="border" select="'1'"/>

<xsl:template match="xtable">
  <table cellspacing="0" cellpadding="2">

    <xsl:attribute name="id">
      <xsl:call-template name="select-renderId"/>
    </xsl:attribute>
    <xsl:attribute name="border">
      <xsl:call-template name="select-border"/>
    </xsl:attribute>

    <xsl:call-template name="xtable-title"/>
    <xsl:apply-templates/>
  </table>
</xsl:template>


<xsl:template name="xtable-title">
  <xsl:if test="@title or @closeId">
    <tr>
      <th colspan="{@visibleColumns}" class="xtable-title">
        <table border="0" cellspacing="0" cellpadding="0" width="100%">
          <tr>
            <xsl:if test="@title">
              <th align="left" class="xtable-title">
                <xsl:value-of select="@title"/>
              </th>
            </xsl:if>

            <xsl:if test="@closeId">
              <td align="right" class="xtable-title">
                <input type="image" src="{$context}/wcf/form/cancel.png" name="{@closeId}" width="16" height="16"/>
              </td>
            </xsl:if>
          </tr>
        </table>
      </th>
    </tr>
  </xsl:if>
</xsl:template>

<xsl:template match="xtr">
  <tr>
    <xsl:apply-templates/>
  </tr>
</xsl:template>

<xsl:template match="xtd">
  <td class="xtable-data">
    <xsl:apply-templates select="@align"/>
    <xsl:apply-templates select="@colspan"/>
    <xsl:apply-templates/>
  </td>
</xsl:template>

<xsl:template match="xth[@sort]">
  <th class="xtable-heading">
    <input type="image" border="0" name="{@id}" src="{$context}/wcf/table/sort-{@sort}.gif" width="9" height="9"/>
    <xsl:text> </xsl:text>
    <xsl:apply-templates/>
  </th>
</xsl:template>

<xsl:template match="xth">
  <th class="xtable-heading">
    <xsl:apply-templates select="@colspan"/>
    <xsl:apply-templates/>
  </th>
</xsl:template>

<xsl:template match="ximgcell[@href]">
  <a href="{@href}">
    <img src="{@src}" border="0" alt="{@alt}"/>
  </a>
</xsl:template>

<xsl:template match="ximgcell">
  <img src="{@src}" border="0" alt="{@alt}"/>
</xsl:template>

<xsl:template match="xpagenav[@direction='current']">
  <!-- img src="{$context}/wcf/table/page-current.gif" width="16" height="16"/ -->
  <b><xsl:value-of select="@page"/></b>
</xsl:template>

<xsl:template match="xpagenav[@direction='forward']">
  <input type="image" border="0" name="{@id}" src="{$context}/wcf/table/page-{@direction}.gif" width="16" height="16"/>
  <xsl:value-of select="@page"/>
</xsl:template>

<xsl:template match="xpagenav[@direction='back']">
  <xsl:value-of select="@page"/>
  <input type="image" border="0" name="{@id}" src="{$context}/wcf/table/page-{@direction}.gif" width="16" height="16"/>
</xsl:template>

</xsl:stylesheet>
