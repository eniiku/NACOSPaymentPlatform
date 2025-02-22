/* prettier-ignore-start */

/* eslint-disable */

// @ts-nocheck

// noinspection JSUnusedGlobalSymbols

// This file is auto-generated by TanStack Router

import { createFileRoute } from '@tanstack/react-router'

// Import Routes

import { Route as rootRoute } from './routes/__root'
import { Route as VerifyErrorImport } from './routes/verify-error'

// Create Virtual Routes

const VerifyLazyImport = createFileRoute('/verify')()
const SuccessLazyImport = createFileRoute('/success')()
const InvoiceLazyImport = createFileRoute('/invoice')()
const FindLazyImport = createFileRoute('/find')()
const IndexLazyImport = createFileRoute('/')()

// Create/Update Routes

const VerifyLazyRoute = VerifyLazyImport.update({
  path: '/verify',
  getParentRoute: () => rootRoute,
} as any).lazy(() => import('./routes/verify.lazy').then((d) => d.Route))

const SuccessLazyRoute = SuccessLazyImport.update({
  path: '/success',
  getParentRoute: () => rootRoute,
} as any).lazy(() => import('./routes/success.lazy').then((d) => d.Route))

const InvoiceLazyRoute = InvoiceLazyImport.update({
  path: '/invoice',
  getParentRoute: () => rootRoute,
} as any).lazy(() => import('./routes/invoice.lazy').then((d) => d.Route))

const FindLazyRoute = FindLazyImport.update({
  path: '/find',
  getParentRoute: () => rootRoute,
} as any).lazy(() => import('./routes/find.lazy').then((d) => d.Route))

const VerifyErrorRoute = VerifyErrorImport.update({
  path: '/verify-error',
  getParentRoute: () => rootRoute,
} as any)

const IndexLazyRoute = IndexLazyImport.update({
  path: '/',
  getParentRoute: () => rootRoute,
} as any).lazy(() => import('./routes/index.lazy').then((d) => d.Route))

// Populate the FileRoutesByPath interface

declare module '@tanstack/react-router' {
  interface FileRoutesByPath {
    '/': {
      id: '/'
      path: '/'
      fullPath: '/'
      preLoaderRoute: typeof IndexLazyImport
      parentRoute: typeof rootRoute
    }
    '/verify-error': {
      id: '/verify-error'
      path: '/verify-error'
      fullPath: '/verify-error'
      preLoaderRoute: typeof VerifyErrorImport
      parentRoute: typeof rootRoute
    }
    '/find': {
      id: '/find'
      path: '/find'
      fullPath: '/find'
      preLoaderRoute: typeof FindLazyImport
      parentRoute: typeof rootRoute
    }
    '/invoice': {
      id: '/invoice'
      path: '/invoice'
      fullPath: '/invoice'
      preLoaderRoute: typeof InvoiceLazyImport
      parentRoute: typeof rootRoute
    }
    '/success': {
      id: '/success'
      path: '/success'
      fullPath: '/success'
      preLoaderRoute: typeof SuccessLazyImport
      parentRoute: typeof rootRoute
    }
    '/verify': {
      id: '/verify'
      path: '/verify'
      fullPath: '/verify'
      preLoaderRoute: typeof VerifyLazyImport
      parentRoute: typeof rootRoute
    }
  }
}

// Create and export the route tree

export interface FileRoutesByFullPath {
  '/': typeof IndexLazyRoute
  '/verify-error': typeof VerifyErrorRoute
  '/find': typeof FindLazyRoute
  '/invoice': typeof InvoiceLazyRoute
  '/success': typeof SuccessLazyRoute
  '/verify': typeof VerifyLazyRoute
}

export interface FileRoutesByTo {
  '/': typeof IndexLazyRoute
  '/verify-error': typeof VerifyErrorRoute
  '/find': typeof FindLazyRoute
  '/invoice': typeof InvoiceLazyRoute
  '/success': typeof SuccessLazyRoute
  '/verify': typeof VerifyLazyRoute
}

export interface FileRoutesById {
  __root__: typeof rootRoute
  '/': typeof IndexLazyRoute
  '/verify-error': typeof VerifyErrorRoute
  '/find': typeof FindLazyRoute
  '/invoice': typeof InvoiceLazyRoute
  '/success': typeof SuccessLazyRoute
  '/verify': typeof VerifyLazyRoute
}

export interface FileRouteTypes {
  fileRoutesByFullPath: FileRoutesByFullPath
  fullPaths:
    | '/'
    | '/verify-error'
    | '/find'
    | '/invoice'
    | '/success'
    | '/verify'
  fileRoutesByTo: FileRoutesByTo
  to: '/' | '/verify-error' | '/find' | '/invoice' | '/success' | '/verify'
  id:
    | '__root__'
    | '/'
    | '/verify-error'
    | '/find'
    | '/invoice'
    | '/success'
    | '/verify'
  fileRoutesById: FileRoutesById
}

export interface RootRouteChildren {
  IndexLazyRoute: typeof IndexLazyRoute
  VerifyErrorRoute: typeof VerifyErrorRoute
  FindLazyRoute: typeof FindLazyRoute
  InvoiceLazyRoute: typeof InvoiceLazyRoute
  SuccessLazyRoute: typeof SuccessLazyRoute
  VerifyLazyRoute: typeof VerifyLazyRoute
}

const rootRouteChildren: RootRouteChildren = {
  IndexLazyRoute: IndexLazyRoute,
  VerifyErrorRoute: VerifyErrorRoute,
  FindLazyRoute: FindLazyRoute,
  InvoiceLazyRoute: InvoiceLazyRoute,
  SuccessLazyRoute: SuccessLazyRoute,
  VerifyLazyRoute: VerifyLazyRoute,
}

export const routeTree = rootRoute
  ._addFileChildren(rootRouteChildren)
  ._addFileTypes<FileRouteTypes>()

/* prettier-ignore-end */

/* ROUTE_MANIFEST_START
{
  "routes": {
    "__root__": {
      "filePath": "__root.tsx",
      "children": [
        "/",
        "/verify-error",
        "/find",
        "/invoice",
        "/success",
        "/verify"
      ]
    },
    "/": {
      "filePath": "index.lazy.tsx"
    },
    "/verify-error": {
      "filePath": "verify-error.tsx"
    },
    "/find": {
      "filePath": "find.lazy.tsx"
    },
    "/invoice": {
      "filePath": "invoice.lazy.tsx"
    },
    "/success": {
      "filePath": "success.lazy.tsx"
    },
    "/verify": {
      "filePath": "verify.lazy.tsx"
    }
  }
}
ROUTE_MANIFEST_END */
