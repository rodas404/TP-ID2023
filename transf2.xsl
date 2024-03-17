<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="global">
    <livros>
      <xsl:apply-templates select="escritor/livros/livro">
        <xsl:sort select="preco" order="descending"/>
      </xsl:apply-templates>
    </livros>
  </xsl:template>

  <xsl:template match="escritor/livros/livro">
    <xsl:if test="position() &lt;= 5">
      <livro>
		<autor>
		 <xsl:value-of select="../../nomeCompleto"/>
		</autor>
        <titulo>
          <xsl:value-of select="titulo"/>
        </titulo>
        <isbn>
          <xsl:value-of select="@isbn"/>
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

