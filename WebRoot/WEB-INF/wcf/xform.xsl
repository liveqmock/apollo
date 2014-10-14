<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- ================= form with 'automatic' 2-column table layout ================= -->

<xsl:param name="border" select="'1'"/>

<xsl:template match="xform[@style='twocolumn']">
  <table border="{$border}" cellspacing="0" cellpadding="2" id="{$renderId}">
    <xsl:apply-templates mode="twocolumn"/>
  </table>
</xsl:template>


<xsl:template mode="twocolumn" match="label|textField|password|listBox1|listBoxN|checkBox|radioButton">
  <xsl:call-template name="show-error"/>
  <tr>
    <td class="xform-label">
      <xsl:value-of select="@label"/>
    </td>
    <td class="xform-input">
      <xsl:apply-templates select="."/>
    </td>
  </tr>
</xsl:template>



<!-- textarea takes the whole space -->
<xsl:template mode="twocolumn" match="textArea">
  <xsl:call-template name="show-error"/>
  <tr>
    <td colspan="2" class="xform-both">
      <xsl:value-of select="@label"/>
      <br/>
      <xsl:apply-templates select="."/>
    </td>
  </tr>
</xsl:template>


<!-- displays a row with buttons -->
<xsl:template mode="twocolumn" match="buttons">
  <tr>
    <td colspan="2" align="right">
      <div align="right">
        <xsl:apply-templates/>
      </div>
    </td>
  </tr>
</xsl:template>


<!-- title text only -->
<xsl:template mode="twocolumn" match="title">
  <tr>
    <th colspan="2" class="xform-title">
      <!-- support both <title>blah</title> and <title value="blah"/> -->
      <xsl:value-of select="@value"/>
      <xsl:apply-templates/>
    </th>
  </tr>
</xsl:template>

<!-- title with close button(s) -->
<xsl:template mode="twocolumn" match="title[imgButton]">
  <tr>
    <th colspan="2" class="xform-title">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <th align="left" class="xform-title">
            <xsl:value-of select="@value"/>
          </th>
          <td align="right" class="xform-close-button">
            <xsl:apply-templates/>
          </td>
        </tr>
      </table>
    </th>
  </tr>
</xsl:template>


<!-- displays text in two columns -->
<xsl:template mode="twocolumn" match="text">
  <tr>
    <td colspan="2" class="xform-both">
      <xsl:value-of select="@value"/>
    </td>
  </tr>
</xsl:template>


<!-- displays a table row with error message -->
<xsl:template name="show-error">
  <xsl:if test="@error">
    <tr>
      <td colspan="2" class="xform-error">
        <xsl:value-of select="@error"/>
      </td>
    </tr>
  </xsl:if>
</xsl:template>

<!-- =============== form without automatic layout ===================== -->

<xsl:template match="xform[@style='manual']">
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="error-list">
  <ul>
    <xsl:for-each select="//*[@error]">
      <li>
        <xsl:value-of select="@error"/>
      </li>
    </xsl:for-each>
  </ul>
</xsl:template>

<xsl:template match="error-message">
  <xsl:value-of select="//@error"/>
</xsl:template>

</xsl:stylesheet>
