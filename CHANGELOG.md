# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [4.2.0] - 2025-02-04

Added DistanceCalculator to calculate between point and line string.

## [4.1.0] - 2025-01-13

Added JtsPolygonFactory with method to create a bounding box polygon.
Added JtsPolygonFactoryRijksdriehoek and JtsPolygonFactoryWgs84 as convenient factories for the rijksdriehoek and wgs84 projections.

## [4.0.0] - 2024-12-12

Breaking change in methods GeodeticCalculatorFactory and BearingCalculator

### Changed

Changed method signature on

```
GeodeticCalculatorFactory.createGeodeticCalculator
BearingCalculator.calculateBearing
```

Removed srid parameter, these calculations always use WGS84 coordinate reference system.<br>
Changed FractionAndDistanceCalculator always uses WGS84 coordinate reference system.

### Added

Validation on a coordinate reference system in FractionAndDistanceCalculator on input line strings.
If no coordinate reference system is provided, it assumes WGS84.

## [3.2.0] - 2024-10-31

### Added
- Added mappers from GeoJson to Jts LineString (generic, wgs84 and rijksdriehoek) 


## [3.0.3] - 2024-09-06

Here we write upgrade notes. It's a team effort to make them as straightforward as possible.

### Added
- MINOR Added this changelog file

### Changed

### Fixed
