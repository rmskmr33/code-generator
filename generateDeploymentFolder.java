private void generateDeploymentFolder(Path deployRoot, Map<String, Object> data) throws IOException {
    // root of releng templates
    Path relengRoot = Paths.get("templates/releng");

    // load from classpath
    try (FileSystem fs = FileSystems.newFileSystem(
            Objects.requireNonNull(getClass().getClassLoader()
                .getResource("templates/releng")).toURI(),
            new HashMap<>()
    )) {
        Path rootInJar = fs.getPath("/templates/releng");

        Files.walk(rootInJar).forEach(path -> {
            try {
                if (Files.isDirectory(path)) {
                    Path targetDir = deployRoot.resolve(rootInJar.relativize(path).toString());
                    Files.createDirectories(targetDir);
                } else {
                    String relative = rootInJar.relativize(path).toString();
                    Path targetFile = deployRoot.resolve(relative.replace(".ftl", ""));
                    generate("releng/" + relative, targetFile, data);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    } catch (Exception e) {
        throw new RuntimeException("Failed to copy releng folder", e);
    }
}
