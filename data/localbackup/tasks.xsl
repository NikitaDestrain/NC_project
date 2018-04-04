<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="journal">
        <xsl:apply-templates select="//entry"/>
    </xsl:template>

    <xsl:template match="entry">
        <xsl:for-each select="value">
            <tr>
                <td>
                    <xsl:value-of select="id/text()"/>
                </td>
                <td>
                    <xsl:value-of select="status/text()"/>
                </td>
                <td>
                    <xsl:value-of select="name/text()"/>
                </td>
                <td>
                    <xsl:value-of select="description/text()"/>
                </td>
                <td>
                    <xsl:value-of select="planned/text()"/>
                </td>
                <td>
                    <xsl:value-of select="notification/text()"/>
                </td>
                <td>
                    <xsl:value-of select="upload/text()"/>
                </td>
                <td>
                    <xsl:value-of select="change/text()"/>
                </td>
            </tr>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>