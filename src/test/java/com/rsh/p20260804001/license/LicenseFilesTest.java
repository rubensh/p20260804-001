package com.rsh.p20260804001.license;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Verifica los documentos de licencia en español e inglés de la feature0019. */
class LicenseFilesTest
{
    /** Documento de licencia en español. */
    private static final Path SPANISH_LICENSE = Path.of("LICENSE-ES.md");

    /** Documento de licencia en inglés. */
    private static final Path ENGLISH_LICENSE = Path.of("LICENSE-EN.md");

    /** Página oficial de la licencia aplicada. */
    private static final String POLYFORM_URL =
            "https://polyformproject.org/licenses/noncommercial/1.0.0";

    /** Nombre del titular de los derechos del proyecto. */
    private static final String COPYRIGHT_HOLDER = "Rubén Santana Hernández";

    /** Comprueba que los dos documentos requeridos existen en la raíz. */
    @Test
    void bilingualLicenseFilesExist()
    {
        assertTrue(Files.isRegularFile(SPANISH_LICENSE), "No existe LICENSE-ES.md");
        assertTrue(Files.isRegularFile(ENGLISH_LICENSE), "No existe LICENSE-EN.md");
    }

    /** Comprueba que el documento inglés contiene el texto completo de PolyForm. */
    @Test
    void englishLicenseContainsOfficialPolyFormTerms() throws IOException
    {
        String license = read(ENGLISH_LICENSE);

        assertTrue(license.contains("# PolyForm Noncommercial License 1.0.0"));
        assertTrue(license.contains(POLYFORM_URL));
        assertTrue(license.contains("## Acceptance"));
        assertTrue(license.contains("## Copyright License"));
        assertTrue(license.contains("## Distribution License"));
        assertTrue(license.contains("## Noncommercial Purposes"));
        assertTrue(license.contains("## Personal Uses"));
        assertTrue(license.contains("## Noncommercial Organizations"));
        assertTrue(license.contains("## No Liability"));
        assertTrue(license.contains("## Definitions"));
    }

    /** Comprueba que el documento español contiene la traducción completa y su prevalencia. */
    @Test
    void spanishLicenseContainsCompleteTranslationAndPrecedenceNotice() throws IOException
    {
        String license = read(SPANISH_LICENSE);

        assertTrue(license.contains("# Licencia PolyForm Noncommercial 1.0.0"));
        assertTrue(license.contains(POLYFORM_URL));
        assertTrue(license.contains("## Aceptación"));
        assertTrue(license.contains("## Licencia de propiedad intelectual"));
        assertTrue(license.contains("## Licencia de distribución"));
        assertTrue(license.contains("## Propósitos no comerciales"));
        assertTrue(license.contains("## Usos personales"));
        assertTrue(license.contains("## Organizaciones no comerciales"));
        assertTrue(license.contains("## Ausencia de responsabilidad"));
        assertTrue(license.contains("prevalecerá el texto original en inglés"));
    }

    /** Comprueba la autoría y los avisos que deben acompañar las redistribuciones. */
    @Test
    void bothLicensesContainRequiredProjectNotices() throws IOException
    {
        for (Path path : new Path[] { SPANISH_LICENSE, ENGLISH_LICENSE })
        {
            String license = read(path);
            assertTrue(license.contains("Copyright 2026 " + COPYRIGHT_HOLDER));
            assertTrue(license.contains("Required Notice:"));
            assertTrue(license.contains("github.com/rubensh/p20260804-001"));
        }
    }

    /** Comprueba que Phaser y el runtime Java quedan excluidos y mantienen sus licencias. */
    @Test
    void thirdPartyComponentsRemainUnderTheirOwnLicenses() throws IOException
    {
        for (Path path : new Path[] { SPANISH_LICENSE, ENGLISH_LICENSE })
        {
            String license = read(path);
            assertTrue(license.contains("webcontent/js/phaser.min.js"));
            assertTrue(license.contains("Phaser 3.90.0"));
            assertTrue(license.contains("The MIT License (MIT)"));
            assertTrue(license.contains("Copyright (c) 2024 Richard Davey, Phaser Studio Inc."));
            assertTrue(license.contains("runtime/legal/java.base/LICENSE"));
            assertTrue(license.contains("ADDITIONAL_LICENSE_INFO"));
            assertTrue(license.contains("ASSEMBLY_EXCEPTION"));
        }
    }

    /** Comprueba que README enlaza las dos licencias. */
    @Test
    void readmeLinksBothLicenseDocuments() throws IOException
    {
        String readme = read(Path.of("README.md"));

        assertTrue(readme.contains("[Licencia en español](LICENSE-ES.md)"));
        assertTrue(readme.contains("[License in English](LICENSE-EN.md)"));
    }

    /** Lee un documento con la codificación del proyecto. */
    private String read(Path path) throws IOException
    {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
