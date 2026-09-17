# Project Statement

## Problem statement

This project explores how low-rank approximation can retain major image structure while representing each color channel with fewer singular components. It provides a local, repeatable SVD image reconstruction workflow suitable for students learning linear algebra and image processing.

## Scope

### In scope

Image validation/loading; RGB and grayscale matrices; truncated SVD; rank, percentage, and quality selection; safe local image output; CLI interaction; statistics; MSE/PSNR; and automated tests.

### Out of scope

Cloud processing, web/mobile interfaces, video or distributed compression, perceptual codecs, and claims that this is a replacement for PNG/JPEG encoding.

## Target users

Students studying SVD or image processing, developers experimenting with matrix approximations, and local users exploring rank-versus-quality trade-offs.

## High-level features

The application separates CLI, validation, image conversion, SVD, output, and metrics responsibilities. It retains a configurable number of leading singular components for each channel and reports both reconstruction quality and final encoded-file measurements.
