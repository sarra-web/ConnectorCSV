package com.keyrus.proxemconnector.connector.csv.configuration.model;

import io.vavr.control.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ConnectorTest {

    @Test
    @DisplayName("configuration must return error if build method is called with invalid id")
    void configuration_must_return_error_if_build_method_is_called_with_invalid_id() {
        final var id = " ";
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.IdMalformed))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with invalid name")
    void configuration_must_return_error_if_build_method_is_called_with_invalid_name() {
        final var id = UUID.randomUUID().toString();
        final var name = " ";
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.NameMalformed))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with invalid separator")
    void configuration_must_return_error_if_build_method_is_called_with_invalid_separator() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = "abc123";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.SeparatorMalformed))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with invalid encoding")
    void configuration_must_return_error_if_build_method_is_called_with_invalid_encoding() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name() + "a";
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.EncodingMalformed))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with invalid folder to scan")
    void configuration_must_return_error_if_build_method_is_called_with_invalid_folder_to_scan() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = "";
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.FolderToScanMalformed))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with invalid archive folder")
    void configuration_must_return_error_if_build_method_is_called_with_invalid_archive_folder() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = " ";
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.ArchiveFolderMalformed))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with invalid failed records folder")
    void configuration_must_return_error_if_build_method_is_called_with_invalid_failed_records_folder() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = " ";
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.FailedRecordsFolderMalformed))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with invalid headers")
    void configuration_must_return_error_if_build_method_is_called_with_invalid_headers() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields = null;

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.IdHeaderMissing))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with headers builders that is created with invalid parameters")
    void configuration_must_return_error_if_build_method_is_called_with_headers_builders_that_is_created_with_invalid_parameters() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                "",
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.HeaderMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.Header))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with valid headers does not contains id")
    void configuration_must_return_error_if_build_method_is_called_with_valid_headers_does_not_contains_id() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                false,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                false,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.IdHeaderMissing))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with valid headers does not cover all positions range")
    void configuration_must_return_error_if_build_method_is_called_with_valid_headers_does_not_cover_all_positions_range() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position * 10,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.HeadersSequenceMalformed))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with valid headers contains positions doubling")
    void configuration_must_return_error_if_build_method_is_called_with_valid_headers_contains_positions_doubling() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(1, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.HeadersSequenceMalformed))
        );
    }

    @Test
    @DisplayName("configuration must return error if build method is called with multiple invalid parameters")
    void configuration_must_return_error_if_build_method_is_called_with_multiple_invalid_parameters() {
        final var id = "";
        final var name = "";
        final var folderToScan = "";
        final var archiveFolder = " ";
        final var failedRecordsFolder = " ";
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                false,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                "",
                                1,
                                UUID.randomUUID().toString(),
                                false,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .getLeft();

        Assertions.assertAll(
                () -> Assertions.assertEquals(7, result.size()),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.IdMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.NameMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.FolderToScanMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.ArchiveFolderMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.FailedRecordsFolderMalformed)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.Header)),
                () -> Assertions.assertTrue(result.stream().anyMatch(it -> it instanceof Connector.Error.HeaderMalformed))
        );
    }

    @Test
    @DisplayName("configuration must return instance if build method is called with valid parameters")
    void configuration_must_return_instance_if_build_method_is_called_with_valid_parameters() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(3, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder1 =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder2 =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                2,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder1)
                        .withHeaders(headerBuilder2)
                        .build()
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(separator, result.separator()),
                () -> Assertions.assertEquals(encoding, result.encoding()),
                () -> Assertions.assertEquals(folderToScan, result.folderToScan()),
                () -> Assertions.assertEquals(archiveFolder, result.archiveFolder()),
                () -> Assertions.assertEquals(failedRecordsFolder, result.failedRecordsFolder()),
                () -> Assertions.assertEquals(containsHeaders, result.containsHeaders()),
                () -> Assertions.assertFalse(result.fields().isEmpty())
        );
    }

    @Test
    @DisplayName("configuration must return instance if build method is called with valid parameters without separator and encoding")
    void configuration_must_return_instance_if_build_method_is_called_with_valid_parameters_without_separator_and_encoding() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(",", result.separator()),
                () -> Assertions.assertEquals(StandardCharsets.UTF_8.name(), result.encoding()),
                () -> Assertions.assertEquals(folderToScan, result.folderToScan()),
                () -> Assertions.assertEquals(archiveFolder, result.archiveFolder()),
                () -> Assertions.assertEquals(failedRecordsFolder, result.failedRecordsFolder()),
                () -> Assertions.assertEquals(containsHeaders, result.containsHeaders()),
                () -> Assertions.assertFalse(result.fields().isEmpty())
        );
    }

    @Test
    @DisplayName("configuration must return instance if build method is called with valid parameters without encoding")
    void configuration_must_return_instance_if_build_method_is_called_with_valid_parameters_without_encoding() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .build()
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(separator, result.separator()),
                () -> Assertions.assertEquals(StandardCharsets.UTF_8.name(), result.encoding()),
                () -> Assertions.assertEquals(folderToScan, result.folderToScan()),
                () -> Assertions.assertEquals(archiveFolder, result.archiveFolder()),
                () -> Assertions.assertEquals(failedRecordsFolder, result.failedRecordsFolder()),
                () -> Assertions.assertEquals(containsHeaders, result.containsHeaders()),
                () -> Assertions.assertFalse(result.fields().isEmpty())
        );
    }

    @Test
    @DisplayName("configuration must return instance if build method is called with null id after initial id in build pipeline")
    void configuration_must_return_instance_if_build_method_is_called_with_null_id_after_initial_id_in_build_pipeline() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .withId(null)
                        .build()
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(separator, result.separator()),
                () -> Assertions.assertEquals(encoding, result.encoding()),
                () -> Assertions.assertEquals(folderToScan, result.folderToScan()),
                () -> Assertions.assertEquals(archiveFolder, result.archiveFolder()),
                () -> Assertions.assertEquals(failedRecordsFolder, result.failedRecordsFolder()),
                () -> Assertions.assertEquals(containsHeaders, result.containsHeaders()),
                () -> Assertions.assertFalse(result.fields().isEmpty())
        );
    }

    @Test
    @DisplayName("configuration must return instance if build method is called with null name after initial name in build pipeline")
    void configuration_must_return_instance_if_build_method_is_called_with_null_name_after_initial_name_in_build_pipeline() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .withName(null)
                        .build()
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(separator, result.separator()),
                () -> Assertions.assertEquals(encoding, result.encoding()),
                () -> Assertions.assertEquals(folderToScan, result.folderToScan()),
                () -> Assertions.assertEquals(archiveFolder, result.archiveFolder()),
                () -> Assertions.assertEquals(failedRecordsFolder, result.failedRecordsFolder()),
                () -> Assertions.assertEquals(containsHeaders, result.containsHeaders()),
                () -> Assertions.assertFalse(result.fields().isEmpty())
        );
    }

    @Test
    @DisplayName("configuration must return instance if build method is called with null separator after initial separator in build pipeline")
    void configuration_must_return_instance_if_build_method_is_called_with_null_separator_after_initial_separator_in_build_pipeline() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .withSeparator(null)
                        .build()
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(separator, result.separator()),
                () -> Assertions.assertEquals(encoding, result.encoding()),
                () -> Assertions.assertEquals(folderToScan, result.folderToScan()),
                () -> Assertions.assertEquals(archiveFolder, result.archiveFolder()),
                () -> Assertions.assertEquals(failedRecordsFolder, result.failedRecordsFolder()),
                () -> Assertions.assertEquals(containsHeaders, result.containsHeaders()),
                () -> Assertions.assertFalse(result.fields().isEmpty())
        );
    }

    @Test
    @DisplayName("configuration must return instance if build method is called with null encoding after initial encoding in build pipeline")
    void configuration_must_return_instance_if_build_method_is_called_with_null_encoding_after_initial_encoding_in_build_pipeline() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .withEncoding(null)
                        .build()
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(separator, result.separator()),
                () -> Assertions.assertEquals(encoding, result.encoding()),
                () -> Assertions.assertEquals(folderToScan, result.folderToScan()),
                () -> Assertions.assertEquals(archiveFolder, result.archiveFolder()),
                () -> Assertions.assertEquals(failedRecordsFolder, result.failedRecordsFolder()),
                () -> Assertions.assertEquals(containsHeaders, result.containsHeaders()),
                () -> Assertions.assertFalse(result.fields().isEmpty())
        );
    }

    @Test
    @DisplayName("configuration must return instance if build method is called with null folder to scan after initial folder to scan in build pipeline")
    void configuration_must_return_instance_if_build_method_is_called_with_null_folder_to_scan_after_initial_folder_to_scan_in_build_pipeline() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .withFolderToScan(null)
                        .build()
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(separator, result.separator()),
                () -> Assertions.assertEquals(encoding, result.encoding()),
                () -> Assertions.assertEquals(folderToScan, result.folderToScan()),
                () -> Assertions.assertEquals(archiveFolder, result.archiveFolder()),
                () -> Assertions.assertEquals(failedRecordsFolder, result.failedRecordsFolder()),
                () -> Assertions.assertEquals(containsHeaders, result.containsHeaders()),
                () -> Assertions.assertFalse(result.fields().isEmpty())
        );
    }

    @Test
    @DisplayName("configuration must return instance if build method is called with null archive folder after initial archive folder in build pipeline")
    void configuration_must_return_instance_if_build_method_is_called_with_null_archive_folder_after_initial_archive_folder_in_build_pipeline() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .withArchiveFolder(null)
                        .build()
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(separator, result.separator()),
                () -> Assertions.assertEquals(encoding, result.encoding()),
                () -> Assertions.assertEquals(folderToScan, result.folderToScan()),
                () -> Assertions.assertEquals(archiveFolder, result.archiveFolder()),
                () -> Assertions.assertEquals(failedRecordsFolder, result.failedRecordsFolder()),
                () -> Assertions.assertEquals(containsHeaders, result.containsHeaders()),
                () -> Assertions.assertFalse(result.fields().isEmpty())
        );
    }

    @Test
    @DisplayName("configuration must return instance if build method is called with null failed records folder after initial failed records folder in build pipeline")
    void configuration_must_return_instance_if_build_method_is_called_with_null_failed_records_folder_after_initial_failed_records_folder_in_build_pipeline() {
        final var id = UUID.randomUUID().toString();
        final var name = UUID.randomUUID().toString();
        final var separator = ";";
        final var encoding = StandardCharsets.US_ASCII.name();
        final var folderToScan = UUID.randomUUID().toString();
        final var archiveFolder = UUID.randomUUID().toString();
        final var failedRecordsFolder = UUID.randomUUID().toString();
        final var containsHeaders = new Random().nextBoolean();
        final Collection<Field> fields =
                IntStream.iterate(2, it -> it + 1)
                        .limit(10)
                        .mapToObj(position ->
                                Field.of(
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                UUID.randomUUID().toString(),
                                                position,
                                                UUID.randomUUID().toString(),
                                                true,
                                                false, "texte"
                                        )
                                        .get()
                        )
                        .collect(Collectors.toUnmodifiableSet());
        final Supplier<Either<Collection<Field.Error>, Field>> headerBuilder =
                () ->
                        Field.of(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                1,
                                UUID.randomUUID().toString(),
                                true,
                                false, "texte"
                        );

        final var result =
                Connector.Builder
                        .builder()
                        .withId(id)
                        .withName(name)
                        .withSeparator(separator)
                        .withEncoding(encoding)
                        .withFolderToScan(folderToScan)
                        .withArchiveFolder(archiveFolder)
                        .withFailedRecordsFolder(failedRecordsFolder)
                        .withContainsHeaders(containsHeaders)
                        .withHeaders(fields)
                        .withHeaders(headerBuilder)
                        .withFailedRecordsFolder(null)
                        .build()
                        .get();

        Assertions.assertAll(
                () -> Assertions.assertEquals(id, result.id()),
                () -> Assertions.assertEquals(name, result.name()),
                () -> Assertions.assertEquals(separator, result.separator()),
                () -> Assertions.assertEquals(encoding, result.encoding()),
                () -> Assertions.assertEquals(folderToScan, result.folderToScan()),
                () -> Assertions.assertEquals(archiveFolder, result.archiveFolder()),
                () -> Assertions.assertEquals(failedRecordsFolder, result.failedRecordsFolder()),
                () -> Assertions.assertEquals(containsHeaders, result.containsHeaders()),
                () -> Assertions.assertFalse(result.fields().isEmpty())
        );
    }
}