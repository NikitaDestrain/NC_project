<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="task">
        <div align="center">
            Task status:
            <xsl:value-of select="status"/>
        </div>
        <div align="center">
            Upload date:
            <xsl:value-of select="upload"/>
        </div>
        <div align="center">
            Change date:
            <xsl:value-of select="change"/>
        </div>
        <table class="table">
            <xsl:template match="name">
                <tr>
                    <td>Name</td>
                    <td>
                        <input type="text" id="editname" class="form-control" name="Name">
                            <xsl:attribute name="value">
                                <xsl:value-of select="name"/>
                            </xsl:attribute>
                        </input>
                    </td>
                </tr>
            </xsl:template>
            <xsl:template match="description">
                <tr>
                    <td>Description</td>
                    <td>
                        <input type="text" class="form-control" name="Description">
                            <xsl:attribute name="value">
                                <xsl:value-of select="description"/>
                            </xsl:attribute>
                        </input>
                    </td>
                </tr>
            </xsl:template>
            <xsl:template match="planned">
                <tr>
                    <td>Planned date</td>
                    <td>
                        <input type="text" class="form-control" name="Planned_date">
                            <xsl:attribute name="value">
                                <xsl:value-of select="planned"/>
                            </xsl:attribute>
                        </input>
                    </td>
                </tr>
            </xsl:template>
            <xsl:template match="notification">
                <tr>
                    <td>Notification date</td>
                    <td>
                        <input type="text" class="form-control" name="Notification_date">
                            <xsl:attribute name="value">
                                <xsl:value-of select="notification"/>
                            </xsl:attribute>
                        </input>
                    </td>
                </tr>
            </xsl:template>
        </table>
    </xsl:template>
</xsl:stylesheet>