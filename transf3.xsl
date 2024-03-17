<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


  <xsl:template match="global">
    <autores>
      <xsl:apply-templates select="escritor">
      </xsl:apply-templates>
    </autores>
  </xsl:template>

  <xsl:template match="escritor">
    <autor>
		<nome>
			<xsl:value-of select="@nome"/>
		</nome>		
		<nomeCompleto>
			<xsl:value-of select="nomeCompleto"/>
		</nomeCompleto>
		<nacionalidade>
			<xsl:value-of select="nacionalidade"/>
		</nacionalidade>
		<fotografia>
			<xsl:value-of select="fotografia"/>
		</fotografia>
		<nascimento>
			<xsl:value-of select="nascimento/ano"/>
		</nascimento>
		<obras>		
		  <xsl:apply-templates select="livros/livro">
		  </xsl:apply-templates>
		</obras>
	</autor>
  </xsl:template>
  
  <xsl:template match="livros/livro">
      <livro>
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
  </xsl:template>
  
</xsl:stylesheet>

