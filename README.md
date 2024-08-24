# CustomEnchantments
Adds some custom enchantments.

## Extension of enchantments
Addition and removal of enchantments requires programming knowledge.

Simply extend `Infusion` class and register it to the `InfusionManager`, and it should work.

You may be interested in `DebuffInfusion`, `SpecialInfusion` and `SetInfusion` for different use cases,
the classes are documented for view.

## Building

The build tool is Maven, dependencies are listed in `pom.xml` and the plugin may be built via `./mvnw package`.
