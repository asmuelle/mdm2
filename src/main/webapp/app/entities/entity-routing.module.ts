import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        data: { pageTitle: 'myApp.customer.home.title' },
        loadChildren: () => import('./customer/customer.routes'),
      },
      {
        path: 'contract',
        data: { pageTitle: 'myApp.contract.home.title' },
        loadChildren: () => import('./contract/contract.routes'),
      },
      {
        path: 'service',
        data: { pageTitle: 'myApp.service.home.title' },
        loadChildren: () => import('./service/service.routes'),
      },
      {
        path: 'scope',
        data: { pageTitle: 'myApp.scope.home.title' },
        loadChildren: () => import('./scope/scope.routes'),
      },
      {
        path: 'peer',
        data: { pageTitle: 'myApp.peer.home.title' },
        loadChildren: () => import('./peer/peer.routes'),
      },
      {
        path: 'meter-import',
        data: { pageTitle: 'myApp.meterImport.home.title' },
        loadChildren: () => import('./meter-import/meter-import.routes'),
      },
      {
        path: 'meter',
        data: { pageTitle: 'myApp.meter.home.title' },
        loadChildren: () => import('./meter/meter.routes'),
      },
      {
        path: 'provider',
        data: { pageTitle: 'myApp.provider.home.title' },
        loadChildren: () => import('./provider/provider.routes'),
      },
      {
        path: 'property-type',
        data: { pageTitle: 'myApp.propertyType.home.title' },
        loadChildren: () => import('./property-type/property-type.routes'),
      },
      {
        path: 'ownership-property',
        data: { pageTitle: 'myApp.ownershipProperty.home.title' },
        loadChildren: () => import('./ownership-property/ownership-property.routes'),
      },
      {
        path: 'namespace',
        data: { pageTitle: 'myApp.namespace.home.title' },
        loadChildren: () => import('./namespace/namespace.routes'),
      },
      {
        path: 'ownership',
        data: { pageTitle: 'myApp.ownership.home.title' },
        loadChildren: () => import('./ownership/ownership.routes'),
      },
      {
        path: 'waste-tracking-parameters',
        data: { pageTitle: 'myApp.wasteTrackingParameters.home.title' },
        loadChildren: () => import('./waste-tracking-parameters/waste-tracking-parameters.routes'),
      },
      {
        path: 'address',
        data: { pageTitle: 'myApp.address.home.title' },
        loadChildren: () => import('./address/address.routes'),
      },
      {
        path: 'country',
        data: { pageTitle: 'myApp.country.home.title' },
        loadChildren: () => import('./country/country.routes'),
      },
      {
        path: 'owner',
        data: { pageTitle: 'myApp.owner.home.title' },
        loadChildren: () => import('./owner/owner.routes'),
      },
      {
        path: 'ownership-classification',
        data: { pageTitle: 'myApp.ownershipClassification.home.title' },
        loadChildren: () => import('./ownership-classification/ownership-classification.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
