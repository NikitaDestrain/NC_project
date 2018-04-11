<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="journalContainer/journals">
        <table>
            <xsl:attribute name="class">
                <xsl:text>table</xsl:text>
            </xsl:attribute>
            <caption>JOURNALS</caption>
            <xsl:for-each select="journal">
                <tr>
                    <td>
                        <xsl:value-of select="id/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="name/text()"/>
                    </td>
                    <td>
                        <xsl:value-of select="description/text()"/>
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>

    <xsl:template match="journalContainer/tasks">
        <table>
            <xsl:attribute name="class">
                <xsl:text>table</xsl:text>
            </xsl:attribute>
            <caption>TASKS</caption>
            <xsl:for-each select="task">
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
                        <xsl:value-of select="name/text()"/>
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
        </table>
    </xsl:template>

</xsl:stylesheet>