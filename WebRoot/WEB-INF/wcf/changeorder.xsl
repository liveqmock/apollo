<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="move-button[@move='fwd']">
  <input border="0" type="image" name="{@id}" src="{$context}/wcf/changeorder/move-down.png" width="9" height="9"/>
</xsl:template>

<xsl:template match="move-button[@move='bwd']">
  <input border="0" type="image" name="{@id}" src="{$context}/wcf/changeorder/move-up.png" width="9" height="9"/>
</xsl:template>

<xsl:template match="move-button">
  <img src="{$context}/wcf/changeorder/move-empty.png" width="9" height="9"/>
</xsl:template>

</xsl:stylesheet>
