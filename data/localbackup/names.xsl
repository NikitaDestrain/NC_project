<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="journalNames">
        <tr>
            <td>Change journal</td>
            <td>
                <select class="form-control" name="journalname">
                    <option value=""/>
                    <xsl:for-each select="name">
                        <option>
                            <xsl:attribute name="value">
                                <xsl:value-of select="text()"/>
                            </xsl:attribute>
                            <xsl:value-of select="text()"/>
                        </option>
                    </xsl:for-each>
                </select>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>