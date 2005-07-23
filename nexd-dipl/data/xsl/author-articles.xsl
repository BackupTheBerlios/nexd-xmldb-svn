<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	            version="1.0">
              
  <xsl:output method="xml" />
				
  <xsl:variable name="nl">
	<xsl:text>
</xsl:text>
  </xsl:variable>

  <xsl:template match="/">
	<myroot>
      <xsl:value-of select="$nl" />
  	  <xsl:apply-templates select="//articles" />
    </myroot>
  </xsl:template>
  
  <xsl:template match="articles">
    <xsl:for-each select="article">
      <xsl:text>  </xsl:text>
      <number><xsl:value-of select="position()" /></number>
      <xsl:value-of select="$nl" />
      <xsl:text>  </xsl:text>
      <title><xsl:value-of select="title/text()" /></title>
      <xsl:value-of select="$nl" />
    </xsl:for-each>
  </xsl:template>
  
</xsl:stylesheet>