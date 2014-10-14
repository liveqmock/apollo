<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="border" select="'1'"/>

<!--
  if this is a nested component, the global stylesheet parameters $renderId and $border
  are not used. Instead, the NodeHandler may define @border and @renderId attributes
-->

<xsl:template match="tree-component">
  <table cellspacing="0">
    <xsl:attribute name="id">
      <xsl:call-template name="select-renderId"/>
    </xsl:attribute>
    <xsl:attribute name="border">
      <xsl:call-template name="select-border"/>
    </xsl:attribute>
    <xsl:attribute name="cellpadding">
      <xsl:call-template name="select-border"/>
    </xsl:attribute>

    <xsl:apply-templates select="tree-node"/>
  </table>
</xsl:template>


<xsl:template match="tree-node">
  <tr>
    <td nowrap="nowrap" class="tree-node-{@style}">

      <div style="margin-left: {@level}em">
        <!-- checkbox / radiobox is handled by controls.xsl -->
        <xsl:apply-templates select="checkBox|radioButton"/>

        <!-- expand/collapse button -->
        <xsl:choose>
          <xsl:when test="@state='expanded'">
            <input type="image" name="{@id}.collapse" src="{$context}/wcf/tree/collapse.png" width="9" height="9"/>
          </xsl:when>
          <xsl:when test="@state='collapsed'">
            <input type="image" name="{@id}.expand" src="{$context}/wcf/tree/expand.png" width="9" height="9"/>
          </xsl:when>
          <xsl:otherwise>
            <img src="{$context}/wcf/tree/leaf.png" width="9" height="9"/>
          </xsl:otherwise>
        </xsl:choose>

        <!-- move up/down button -->
        <xsl:apply-templates select="move-button"/>

        <xsl:text> </xsl:text>
        <xsl:choose>
          <xsl:when test="@hrefId">
            <a href="?{@hrefId}=x">
              <xsl:value-of select="@label"/>
            </a>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="@label"/>
          </xsl:otherwise>
        </xsl:choose>
      </div>
    </td>
  </tr>
  <xsl:apply-templates select="tree-node"/>
</xsl:template>


</xsl:stylesheet>
