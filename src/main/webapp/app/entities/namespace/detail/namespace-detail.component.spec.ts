import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { NamespaceDetailComponent } from './namespace-detail.component';

describe('Namespace Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NamespaceDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: NamespaceDetailComponent,
              resolve: { namespace: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(NamespaceDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load namespace on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', NamespaceDetailComponent);

      // THEN
      expect(instance.namespace).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
