<?xml version="1.0" encoding="UTF-8"?>

<!-- New XSLT document created with EditiX XML Editor (http://www.editix.com) at Sat May 22 17:58:30 WEST 2021 -->

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <livros>
      <xsl:apply-templates select="livro">
        <xsl:sort select="preco" order="descending"/>
      </xsl:apply-templates>
    </livros>
  </xsl:template>

  <xsl:template match="livro">
    <xsl:if test="position() &lt;= 5">
      <livro>
        <titulo>
          <xsl:value-of select="titulo"/>
        </titulo>
        <isbn>
          <xsl:value-of select="isbn"/>
        </isbn>
        <preco>
          <xsl:value-of select="preco"/>
        </preco>
        <editora>
          <xsl:value-of select="editora"/>
        </editora>
        <capa>
          <xsl:value-of select="capa"/>
        </capa>
      </livro>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>








