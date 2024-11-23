package org.open.ngelmakproject.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Sevice for saving and retrieving files.
 * 
 * Learn more about Spring-Boot storage please check:
 * https://spring.io/guides/gs/uploading-files
 * 
 * @author yusufaye
 */
@Service
public class FileStorageService {

  /**
   * Folder location for storing files
   */
  private static String root = "resources/upload-dir";

  public final static Path buildPath(String... dirs) {
    return Paths.get(root, dirs);
  }

  public void init(Path path) {
    try {
      Files.createDirectories(path);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }

  /**
   * Save file to the local folder.
   * 
   * @param file
   */
  public final Path store(MultipartFile file, String filename, String... dirs) {
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file.");
      }
      Path destinationFile = FileStorageService.buildPath(dirs).resolve(filename).normalize()
          .toAbsolutePath();
      if (!Files.exists(destinationFile.getParent())) {
        this.init(destinationFile.getParent());
      }
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return destinationFile;
      }
    } catch (IOException e) {
      throw new StorageException("Failed to store file.", e);
    }
  }

  public Path load(String filename, String... dirs) {
    return FileStorageService.buildPath(dirs).resolve(filename);
  }

  public Stream<Path> loadAll(String... dirs) {
    final Path location = FileStorageService.buildPath(dirs);
    try {
      return Files.walk(location, 1)
          .filter(path -> !path.equals(location))
          .map(location::relativize);
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }
  }

  public Resource loadAsResource(String filename, String... dirs) {
    try {
      Path file = load(filename, dirs);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);
      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  public void deleteFile(String filename, String... dirs) throws IOException {
    FileSystemUtils.deleteRecursively(load(filename, dirs));
  }

  public void deleteDirectory(String... dirs) {
    FileSystemUtils.deleteRecursively(FileStorageService.buildPath(dirs).toFile());
  }
}
